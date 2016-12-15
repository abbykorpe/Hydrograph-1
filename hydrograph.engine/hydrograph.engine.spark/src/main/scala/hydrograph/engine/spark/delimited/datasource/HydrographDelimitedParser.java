package hydrograph.engine.spark.delimited.datasource;

import hydrograph.engine.spark.datasource.utils.TypeCast;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.regex.Pattern;

import org.apache.spark.sql.types.StructType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HydrographDelimitedParser implements Serializable {
    private static Logger LOG= LoggerFactory.getLogger(HydrographDelimitedParser.class);

    private static final long serialVersionUID = 4546944494735373827L;


    static final String SPECIAL_REGEX_CHARS = "([\\]\\[|.*<>\\\\$^?()=!+])";

    static final String QUOTED_REGEX_FORMAT = "%2$s(?=(?:[^%1$s]*%1$s[^%1$s]*[^%1$s%2$s]*%1$s)*(?![^%1$s]*%1$s))";

    static final String CLEAN_REGEX_FORMAT = "^(?:%1$s)(.*)(?:%1$s)$";

    static final String ESCAPE_REGEX_FORMAT = "(%1$s%1$s)";

    protected Pattern splitPattern;

    protected Pattern cleanPattern;

    protected Pattern escapePattern;

    protected String delimiter;

    protected String quote;

    protected boolean strict = true; // need to cache value across resets

    protected boolean enforceStrict = true;

    protected int numValues;

    protected Type[] types;

    protected boolean safe = true;

    protected StructType schema;

    protected String[] dateFormats;

    public HydrographDelimitedParser(String delimiter, String quote, Class[] types, String[] dateFormats,StructType schema) {
        reset(delimiter, quote, types, strict, safe, dateFormats, schema);
    }

    public HydrographDelimitedParser(String delimiter, String quote, Class[] types, boolean strict, boolean safe, String[] dateFormats, StructType schema) {
        reset(delimiter, quote, types, strict, safe, dateFormats, schema/*, null, null*/);
    }

    public void reset(String delimiter, String quote, Type[] types, boolean strict, boolean safe, String[] dateFormats, StructType schema) {
        if (delimiter == null || delimiter.isEmpty())
            throw new IllegalArgumentException("delimiter may not be null or empty");

        if (delimiter.equals(quote))
            throw new IllegalArgumentException("delimiter and quote character may not be the same value, got: '" + delimiter + "'");

        this.delimiter = delimiter;
        this.strict = strict;
        this.safe = safe;
        this.schema = schema;
        this.dateFormats = dateFormats;

        if (quote != null && !quote.isEmpty()) // if empty, leave null
            this.quote = quote;

        if (types != null && types.length == 0)
            this.types = null;

        if (types != null)
            this.types = Arrays.copyOf(types, types.length);


        this.numValues = schema.length();
        this.enforceStrict = this.strict;

        splitPattern = createSplitPatternFor(this.delimiter, this.quote);
        cleanPattern = createCleanPatternFor(this.quote);
        escapePattern = createEscapePatternFor(this.quote);

    }
    public String getDelimiter() {
        return delimiter;
    }

    public String getQuote() {
        return quote;
    }

    /**
     * Method createEscapePatternFor creates a regex {@link Pattern} cleaning quote escapes from a String.
     * <p/>
     * If {@code quote} is null or empty, a null value will be returned;
     *
     * @param quote of type String
     * @return Pattern
     */
    public Pattern createEscapePatternFor(String quote) {
        if (quote == null || quote.isEmpty())
            return null;

        return Pattern.compile(String.format(ESCAPE_REGEX_FORMAT, quote));
    }

    /**
     * Method createCleanPatternFor creates a regex {@link Pattern} for removing quote characters from a String.
     * <p/>
     * If {@code quote} is null or empty, a null value will be returned;
     *
     * @param quote of type String
     * @return Pattern
     */
    public Pattern createCleanPatternFor(String quote) {
        if (quote == null || quote.isEmpty())
            return null;

        return Pattern.compile(String.format(CLEAN_REGEX_FORMAT, quote));
    }

    /**
     * Method createSplitPatternFor creates a regex {@link Pattern} for splitting a line of text into its component
     * parts using the given delimiter and quote Strings. {@code quote} may be null.
     *
     * @param delimiter of type String
     * @param quote     of type String
     * @return Pattern
     */
    public Pattern createSplitPatternFor(String delimiter, String quote) {
        String escapedDelimiter = delimiter.replaceAll(SPECIAL_REGEX_CHARS, "\\\\$1");

        if (quote == null || quote.isEmpty())
            return Pattern.compile(escapedDelimiter);
        else
            return Pattern.compile(String.format(QUOTED_REGEX_FORMAT, quote, escapedDelimiter));
    }

    /**
     * Method createSplit will split the given {@code value} with the given {@code splitPattern}.
     *
     * @param value        of type String
     * @param splitPattern of type Pattern
     * @param numValues    of type int
     * @return String[]
     */
    public String[] createSplit(String value, Pattern splitPattern, int numValues) {
        return splitPattern.split(value, numValues);
    }

    /**
     * Method cleanSplit will return a quote free array of String values, the given {@code split} array
     * will be updated in place.
     * <p/>
     * If {@code cleanPattern} is null, quote cleaning will not be performed, but all empty String values
     * will be replaces with a {@code null} value.
     *
     * @param split         of type Object[]
     * @param cleanPattern  of type Pattern
     * @param escapePattern of type Pattern
     * @param quote         of type String
     * @return Object[] as a convenience
     */
    public Object[] cleanSplit(Object[] split, Pattern cleanPattern, Pattern escapePattern, String quote) {
        if (cleanPattern != null) {
            for (int i = 0; i < split.length; i++) {
                split[i] = cleanPattern.matcher((String) split[i]).replaceAll("$1");
                split[i] = escapePattern.matcher((String) split[i]).replaceAll(quote);
            }
        }

        for (int i = 0; i < split.length; i++) {
            if (((String) split[i]).isEmpty())
                split[i] = null;
        }

        return split;
    }

    public Object[] parseLine(String line) {

        Object[] split = onlyParseLine(line);

        split = cleanParsedLine(split);

        return coerceParsedLine( line, split );
    }


    private Object[] coerceParsedLine(String line, Object[] split) {

        Object[] result = new Object[split.length];
        for (int i = 0; i < split.length; i++) {
            try {
                split[i] = !schema.apply(i).dataType().simpleString().equalsIgnoreCase("String") ? split[i].toString().trim() : split[i];
                result[i] = TypeCast.castingInputData(split[i], schema.apply(i).dataType(),
                        schema.apply(i).nullable(), "null", true, dateFormats[i]);
            } catch (Exception exception) {
                result[i] = null;
                if (!safe) {
                    LOG.error(getSafeMessage(split[i], i) + "\n Line being parsed => " + line);
                    throw new RuntimeException(getSafeMessage(split[i], i) + "\n Line being parsed => " + line);
                }
            }
        }
        split = result;

        return split;
    }
    protected Object[] cleanParsedLine(Object[] split) {
        return cleanSplit(split, cleanPattern, escapePattern, quote);
    }

    private String getSafeMessage(Object object, int i)
    {
        try
        {
            return "field " + schema.apply(i).name() + " cannot be coerced from : " + object + " to: " + schema.apply(i).dataType();
        }
        catch( Throwable throwable )
        {
            return "field pos " + i + " cannot be coerced from: " + object + ", pos has no corresponding field name or coercion type";
        }
    }

    protected Object[] onlyParseLine(String line) {
        Object[] split = createSplit(line, splitPattern, numValues == 0 ? 0 : -1);

        if (numValues != 0 && split.length != numValues) {

            if( enforceStrict ){
                LOG.error(getParseMessage( split ) );
                throw new RuntimeException( getParseMessage( split ) ); // trap actual line data
            }

            Object[] array = new Object[numValues];
            Arrays.fill(array, "");
            System.arraycopy(split, 0, array, 0, Math.min(numValues, split.length));

            split = array;
        }

        return split;
    }

    private String getParseMessage( Object[] split )
    {
        return "did not parse correct number of values from input data, expected: " + numValues + ", got: " + split.length + ":" + Arrays.toString(split);
    }

}
