package hydrograph.ui.graph.editor;

import org.eclipse.gef.palette.PaletteDrawer;
import org.eclipse.gef.palette.PaletteRoot;
import org.junit.Assert;
import org.junit.Test;

public class PaletteViewTest {
	 
	 
	
@Test
public void itShouldCreateComponentCategory()
{
//given
String componentCategoryName ="Input";
ELTGraphicalEditor eltGraphEditor=new ELTGraphicalEditor();
//when

PaletteDrawer p=eltGraphEditor.createPaletteContainer(componentCategoryName);
//
Assert.assertTrue(p.isVisible());
}

@Test
public void itShouldAddComponentCategoryToPalette()
{
//given
String componentCategoryName ="Input";
ELTGraphicalEditor eltGraphEditor=new ELTGraphicalEditor();
PaletteDrawer p=eltGraphEditor.createPaletteContainer(componentCategoryName);
PaletteRoot paletteRoot=new PaletteRoot();
//when
eltGraphEditor.addContainerToPalette(paletteRoot,p);
//
Assert.assertTrue(paletteRoot.getChildren().contains(p));
}
}
