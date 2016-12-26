/*******************************************************************************
 * Copyright 2016 Capital One Services, LLC and Bitwise, Inc.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package hydrograph.engine.spark.helper;

import hydrograph.engine.spark.datasource.utils.TypeCast;
import org.apache.spark.sql.types.StructType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Arrays;

@SuppressWarnings("rawtypes")
public class DelimitedAndFixedWidthHelper {

    private DelimitedAndFixedWidthHelper() {

    }

    static int counter = 0;
    static boolean isFixedWidthField = false;
    private static final Logger LOG = LoggerFactory
            .getLogger(DelimitedAndFixedWidthHelper.class);

    public static Object[] getFields(
            StructType schema, String line,
            String[] lengthsAndDelimiters, String[] lengthsAndDelimitersType,
            boolean safe, String quote, String[] dateFormats) {

        if (!line.equals("")) {

            try {
                String[] tokens = generateTokensFromRawData(line,
                        lengthsAndDelimiters, lengthsAndDelimitersType, quote);
//                Type[] fieldDataTypes = types;
//                if (fieldDataTypes == null) {
//                    fieldDataTypes = new Type[sourceFields.size()];
//                    for (int i = 0; i < sourceFields.size(); i++) {
//                        fieldDataTypes[i] = String.class;
//                    }
//                }
//                private Object[] coerceParsedTokens(
//                        String line, Object[] tokens, boolean safe,
//                StructType schema, String[] dateFormats) {
                return coerceParsedTokens(line, (Object[]) tokens, safe,schema, dateFormats);
            } catch (Exception e) {
                LOG.error(
                        "Exception while generating tokens.\nLine being parsed: "
                                + line + "\nFields: " + getFieldsFromSchema(schema,dateFormats.length)
                                + "\nLengths and delimiters in scheme: "
                                + Arrays.toString(lengthsAndDelimiters)
                                + "\nDatatypes in scheme: "
                                + getTypesFromSchema(schema,dateFormats.length)
                                + "\nSafe was set to: " + safe, e);
                throw new RuntimeException(e);
            }
        } else {
            return new Object[lengthsAndDelimiters.length];
        }
    }

    private static String getTypesFromSchema(StructType schema, int length) {
        String fields = schema.apply(0).dataType().toString();
        for(int i=1;i< length ;i++){
            fields = fields + schema.apply(i).dataType().toString();
        }
        return fields;
    }

    private static String getFieldsFromSchema(StructType schema, int length) {
        String fields = schema.apply(0).name();
        for(int i=1;i< length ;i++){
            fields = fields + schema.apply(i).name();
        }
        return fields;
    }

    private static String[] generateTokensFromRawData(
            String line,String[] lengthsAndDelimiters,
            String[] lengthsAndDelimitersType,String quote) {
        String tokens[] = new String[lengthsAndDelimiters.length];
        String strings[];
        String identifier;
        quote = DelimitedAndFixedWidthHelper.maskRegexChar(quote);
        for (int i = 0; i < lengthsAndDelimiters.length; i++) {
            identifier = DelimitedAndFixedWidthHelper
                    .maskRegexChar(lengthsAndDelimiters[i]);
            if (lengthsAndDelimitersType[i].contains("Integer")) {
                tokens[i] = line.substring(0, Integer.parseInt(identifier));
                if (i != (lengthsAndDelimiters.length - 1))
                    line = line.substring(Integer.parseInt(identifier));
            } else {
                if (!"".equals(quote) && line.contains(quote.replace("\\", ""))) {
                    // Creation of RegEx to split data based on delimiter
                    // ignoring the delimiter present in data based on
                    // presence of quote char
                    identifier = identifier + "(?=(?:[^" + quote + "]*" + quote
                            + "[^" + quote + "]*[^" + quote + identifier + "]*"
                            + quote + ")*(?![^" + quote + "]*" + quote + "))";
                }
                strings = line.split(identifier);
                if (strings.length != 0) {
                    tokens[i] = ((strings)[0]).replace(quote.replace("\\", ""),
                            "");
                    if (i != (lengthsAndDelimiters.length - 1))
                        line = (line.split(identifier, 2))[1];
                } else {
                    tokens[i] = "";
                }
            }
        }
        return tokens;
    }

//    private static Object[] coerceParsedTokens(
//            String line, boolean safe, String[] tokens,
//            StructType schema) {
//        Object[] coercedTokens = new Object[tokens.length];
//        for (int i = 0; i < tokens.length; i++) {
//            try {
//                if (coercions[i] instanceof StringCoerce) {
//                    coercedTokens[i] = coercions[i].canonical(tokens[i]);
//                } else if (coercions[i] instanceof DateType
//                        && tokens[i] != null && !"".equals(tokens[i])) {
//                    coercedTokens[i] = coercions[i].canonical(tokens[i]);
//                } else
//                    coercedTokens[i] = coercions[i].canonical(tokens[i].trim()
//                            .length() > 0 ? tokens[i].trim() : null);
//            } catch (Exception exception) {
//                String message = "field " + sourceFields.get(i)
//                        + " cannot be coerced from : " + tokens[i] + " to: "
//                        + fieldDataTypes[i];
//                coercedTokens[i] = null;
//                if (!safe) {
//                    throw new RuntimeException(message, exception, new Tuple(line));
//                }
//            }
//        }
//        return coercedTokens;
//    }

    private static Object[] coerceParsedTokens(
            String line, Object[] tokens, boolean safe,
            StructType schema, String[] dateFormats) {

        Object[] result = new Object[tokens.length];
        for (int i = 0; i < tokens.length; i++) {
            try {
                tokens[i] = !schema.apply(i).dataType().simpleString().equalsIgnoreCase("String") ? tokens[i].toString().trim() : tokens[i];
                result[i] = TypeCast.castingInputData(tokens[i], schema.apply(i).dataType(),
                        schema.apply(i).nullable(), "null", true, dateFormats[i]);
            } catch (Exception exception) {
                result[i] = null;
                if (!safe) {
                    LOG.error(getSafeMessage(tokens[i], i, schema) + "\n Line being parsed => " + line);
                    throw new RuntimeException(getSafeMessage(tokens[i], i, schema) + "\n Line being parsed => " + line);
                }
            }
        }
        tokens = result;
        return tokens;
    }

    private static String getSafeMessage(
            Object object, int i, StructType schema) {
        try {
            return "field " + schema.apply(i).name() + " cannot be coerced from : " + object + " to: " + schema.apply(i).dataType();
        } catch (Throwable throwable) {
            return "field pos " + i + " cannot be coerced from: " + object + ", pos has no corresponding field name or coercion type";
        }
    }

//	public static StringBuilder createLine(Tuple tuple,
//			String[] lengthsAndDelimiters, String[] lengthsAndDelimitersType,
//			boolean strict, char filler, Type[] types, String quote) {
//		counter = 0;
//		StringBuilder buffer = new StringBuilder();
//		for (Object value : tuple) {
//			isFixedWidthField = false;
//			isFixedWidthField = isFixedWidthField(lengthsAndDelimitersType,
//					counter);
//
//			// to apply datatype while writing the file
//			if (types != null) {
//				if (types[counter] instanceof DateType) {
//					value = Coercions.coercibleTypeFor(types[counter]).coerce(
//							value, String.class);
//				} else {
//					value = Coercions.coercibleTypeFor(types[counter])
//							.canonical(value);
//				}
//			}
//
//			if (value == null) {
//				value = "";
//			}
//
//			if (isFixedWidthField) {
//				int lengthDifference = value.toString().length()
//						- Integer.parseInt(lengthsAndDelimiters[counter]);
//				if (lengthDifference == 0) {
//					buffer.append(value);
//					counter++;
//					continue;
//				} else if (lengthDifference > 0) {
//					if (strict) {
//						throw new RuntimeException(
//								"Fixed width field write error. Field "
//										+ value
//										+ " has length "
//										+ value.toString().length()
//										+ " whereas provided is "
//										+ lengthsAndDelimiters[counter]
//										+ ". Set strict to false and provide filler to overide such errors if this is expected behaviour.",
//								new Tuple(tuple));
//					}
//
//					buffer.append(value.toString().substring(0,
//							Integer.parseInt(lengthsAndDelimiters[counter])));
//					counter++;
//					continue;
//				} else if (lengthDifference < 0) {
//					if (strict) {
//						throw new RuntimeException(
//								"Fixed width field write error. Field "
//										+ value
//										+ " has length "
//										+ value.toString().length()
//										+ " whereas provided is "
//										+ lengthsAndDelimiters[counter]
//										+ ". Set strict to false and provide filler to overide such errors if this is expected behaviour.",
//								tuple.toString());
//					}
//					try {
//						if (isNumeric(value)) {
//							appendZero(buffer, lengthDifference * -1);
//							buffer.append(value);
//						} else {
//							buffer.append(value);
//							appendFiller(buffer, filler, lengthDifference * -1);
//						}
//					} catch (IOException e) {
//						LOG.error("", e);
//						throw new RuntimeException(e);
//					}
//					counter++;
//					continue;
//				}
//			}
//			if (quoteCharPresent(quote)) {
//				value = appendQuoteChars(value, quote,
//						lengthsAndDelimiters[counter]);
//			}
//			buffer.append(value);
//			if (lengthsAndDelimiters[counter].contentEquals("\\n"))
//				lengthsAndDelimiters[counter] = "\n";
//			if (lengthsAndDelimiters[counter].contentEquals("\\t"))
//				lengthsAndDelimiters[counter] = "\t";
//			if (lengthsAndDelimiters[counter].contentEquals("\\r"))
//				lengthsAndDelimiters[counter] = "\r";
//			buffer.append(parseHex(lengthsAndDelimiters[counter]));
//			counter++;
//		}
//		return buffer;
//	}

    private static boolean quoteCharPresent(String quote) {
        return !quote.equals("");
    }

    private static Object appendQuoteChars(Object value, String quote,
                                           String lengthsAndDelimiters) {
        if (value instanceof String && ((String) value).contains(lengthsAndDelimiters)) {
            value = quote + ((String) value) + quote;
        }
        return value;
    }

    private static boolean isFixedWidthField(String[] lengthsAndDelimitersType,
                                             int counter) {
        return lengthsAndDelimitersType[counter].contains("Integer");
    }

    private static boolean isNumeric(Object value) {
        return value instanceof Number;
    }

    private static void appendZero(Appendable buffer, int times)
            throws IOException {
        char filler = ' ';
        for (int i = 0; i < times; i++) {
            buffer.append(filler);
        }
    }

    private static void appendFiller(Appendable buffer, char filler, int times)
            throws IOException {
        for (int i = 0; i < times; i++) {
            buffer.append(filler);
        }
    }

    public static boolean isLastFieldNewLine(String[] lengthsAndDelimiters) {
        return lengthsAndDelimiters[lengthsAndDelimiters.length - 1]
                .matches("\n")
                || lengthsAndDelimiters[lengthsAndDelimiters.length - 1]
                .contentEquals("\\n");
    }

    public static boolean hasaNewLineField(String[] lengthsAndDelimiters) {
        for (String string : lengthsAndDelimiters) {
            if (string.contains("\n") || string.contentEquals("\\n"))
                return true;
        }
        return false;
    }

    public static String modifyIdentifier(String identifier) {
        String string = identifier;
        if (identifier.contains("\\r\\n")) {
            string = identifier.replace("\\r\\n", "\r\n");
        } else if (identifier.contains("\\n")) {
            string = identifier.replace("\\n", "\n");
        }
        if (identifier.contains("\\t")) {
            string = identifier.replace("\\t", "\t");
        }
        if (identifier.contains("\\x")) {
            string = parseHex(identifier);
        }
        return string;
    }

    public static String[] modifyIdentifier(String[] identifiers) {
        for (int i = 0; i < identifiers.length; i++) {
            identifiers[i] = modifyIdentifier(identifiers[i]);
        }
        return identifiers;
    }

    public static String spillOneLineToOutput(StringBuilder sb,
                                              String[] lengthsAndDelimiters) {
        String line = "";
        if (!isLastFieldNewLine(lengthsAndDelimiters)
                && !isLastFixedWidthFieldNewLineField(lengthsAndDelimiters)) {
            if (hasaNewLineField(lengthsAndDelimiters)) {
                String[] splits = sb.toString().split("\n");
                for (int i = 0; i < splits.length; i++) {
                    if (i != splits.length - 1) {
                        line += splits[i];
                        line += "\n";
                    }

                }
                return line.substring(0, line.length() - 1);
            }
        } else {
            sb.replace(sb.length() - 1, sb.length(), "");
            return sb.toString();
        }
        return line;
    }

    public static boolean isLastFixedWidthFieldNewLineField(
            String[] lengthsAndDelimiters) {
        try {
            return Integer
                    .parseInt(lengthsAndDelimiters[lengthsAndDelimiters.length - 1]) == 1;
        } catch (Exception e) {
            return false;
        }
    }

    public static String maskRegexChar(
            String singleChar) {
        String string = singleChar;
        if (singleChar.contains("|")) {
            string = singleChar.replace("|", "\\|");
        }
        if (singleChar.contains(".")) {
            string = singleChar.replace(".", "\\.");
        }
        if (singleChar.contains("+")) {
            string = singleChar.replace("+", "\\+");
        }
        if (singleChar.contains("$")) {
            string = singleChar.replace("$", "\\$");
        }
        if (singleChar.contains("*")) {
            string = singleChar.replace("*", "\\*");
        }
        if (singleChar.contains("?")) {
            string = singleChar.replace("?", "\\?");
        }
        if (singleChar.contains("^")) {
            string = singleChar.replace("^", "\\^");
        }
        if (singleChar.contains("-")) {
            string = singleChar.replace("-", "\\-");
        }
        if (singleChar.contains("\\x")) {
            string = parseHex(singleChar);
        }
        return string;
    }

    public static String[] checkIfDelimiterIsRegexChar(
            String[] lengthsAndDelimiters) {
        for (int i = 0; i < lengthsAndDelimiters.length; i++)
            lengthsAndDelimiters[i] = maskRegexChar(lengthsAndDelimiters[i]);
        return lengthsAndDelimiters;
    }

    public static String arrayToString(String[] lengthsAndDelimiters) {
        String string = "";
        for (String str : lengthsAndDelimiters) {
            string += str;
            string += "comma";
        }
        return string;
    }

    public static String[] stringToArray(String string) {
        return string.split("comma");
    }

    public static String parseHex(String input) {
        final int NO_OF_DIGITS = 2;

        if (input.contains("\\t")) {
            input = input.replace("\\t", "\\x09");
        }

        String[] tokens = input.split("\\\\x");
        String hex;
        String temp;
        boolean startsWithHex = input.startsWith("\\x");

        for (int counter = 0; counter < tokens.length; counter++) {

            if (counter == 0 && !startsWithHex)
                continue;

            if (tokens[counter].equals(""))
                continue;

            temp = tokens[counter];
            hex = temp.substring(0, NO_OF_DIGITS);
            temp = temp.substring(NO_OF_DIGITS, temp.length());
            tokens[counter] = hexToChar(hex) + temp;

        }

        String result = "";
        for (String token : tokens) {
            result = result + token;
        }
        return result;
    }

    private static char hexToChar(String hex) {
        return (char) Short.parseShort(hex, 16);
    }
}