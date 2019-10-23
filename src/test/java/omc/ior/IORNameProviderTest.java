package omc.ior;

import static org.testng.Assert.assertEquals;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import omc.Global;

public class IORNameProviderTest {
  
  Path stdPath = (Global.isLinuxOS() || Global.isMacOS()) ?
      Global.tmpDir.resolve("openmodelica." + Global.username + ".objid") :
      Global.tmpDir.resolve("openmodelica.objid");

  
  Path[] tmpFiles = new Path[]{
      Paths.get(stdPath.toString()+".unique"),
      Paths.get(stdPath.toString()+".unique-2"),
      Paths.get(stdPath.toString()+".unique-3")};
  
  @BeforeClass
  public void createTmpFiles() throws IOException {
    for(Path tmp : tmpFiles)
      Files.createFile(tmp);
  }
  
  @AfterClass
  public void removeTmpFiles() throws IOException {
    for(Path tmp : tmpFiles)
      Files.delete(tmp);
  }
    
  @Test
  public void testStdProvider() {
    IORNameProvider provider = new StdIORNameProvider();
    assertEquals(provider.getPath(), stdPath);
    assertEquals(provider.getSuffix(), Optional.empty());
  }
  
  @Test
  public void testCustomProviderNonUnique() {
    IORNameProvider provider = new CustomIORNameProvider("test", false);
    assertEquals(provider.getSuffix(), Optional.of("test"));
    assertEquals(provider.getPath(), Paths.get(stdPath.toString()+".test"));
  }

  @Test
  public void testCustomProviderUniqueSimple() throws IOException {
    IORNameProvider provider = new CustomIORNameProvider("xyyz", true);
    assertEquals(provider.getSuffix(), Optional.of("xyyz"));
    assertEquals(provider.getPath(), Paths.get(stdPath.toString()+".xyyz"));
  }

  
  @Test
  public void testCustomProviderUnique() throws IOException {
    //test
    IORNameProvider provider = new CustomIORNameProvider("unique", true);
    assertEquals(provider.getSuffix(), Optional.of("unique-4"));
    assertEquals(provider.getPath(), Paths.get(stdPath.toString()+".unique-4"));
  }
}
