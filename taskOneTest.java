package task1;


import org.junit.*;
import task1.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class taskOneTest {

	@Before
    public void setup() throws Exception {
        clearBufferOutput();
    }

    private void clearBufferOutput() throws Exception {
        Field bufferField = TaskOne.class.getDeclaredField("bufferOutput");
        bufferField.setAccessible(true);
        List<String> buffer = (List<String>) bufferField.get(null); // Get the static field
        buffer.clear(); // Clear the list before each test
    }
    
    @Test
    public void testSingleCommandSort() {
        TaskOne task = new TaskOne();
        task.executeCommands("sort taskA.csv");
        List<String> output = task.getCommandOutput();
        System.out.println("command output size " + output.size() + ", index 0 = " + output.get(0));
        Assert.assertEquals(22, output.size());
        Assert.assertEquals("Corrosion,44,Ravager,C,Neophyte", output.get(0));
        Assert.assertEquals("Wereblood,23,Chalice,G,Advisor", output.get(21));
    }

    @Test
    public void testSingleCommandWC() {
        TaskOne task = new TaskOne();
        task.executeCommands("wc taskA.csv");
        List<String> output = task.getCommandOutput();
        System.out.println("command output size " + output.size() + ", index 0 = " + output.get(0));
        Assert.assertEquals(1, output.size());
        
        String expected1 = "22 22 636"; // if hidden characters are not counted
        String expected2 = "21 22 678"; // if only newlines are counted (this is Linux OS output)
    
        Assert.assertTrue(
        "Output should be either '" + expected1 + "' or '" + expected2 + "', but got: " + output.get(0),
        output.get(0).equals(expected1) || output.get(0).equals(expected2));

    }

    @Test
    public void testSingleCommandCat() {
        TaskOne task = new TaskOne();
        task.executeCommands("cat taskA.csv");
        List<String> output = task.getCommandOutput();
        System.out.println("command output size " + output.size() + ", index 0 = " + output.get(0));
        Assert.assertEquals(22, output.size());
        Assert.assertEquals("Inferno,43,Limbo,A,Advisor", output.get(0));
    }

    @Test
    public void testMultipleCommands() {
        TaskOne task = new TaskOne();
        task.executeCommands("cat taskA.csv | sort | uniq");
        List<String> output = task.getCommandOutput();
        assertEquals(12, output.size());
        assertEquals("Corrosion,44,Ravager,C,Neophyte", output.get(0));
        assertEquals("Wereblood,23,Chalice,G,Advisor", output.get(11));
       assertEquals("Fierce,5,Deluge,G,Duke", output.get(1));
    }


    @Test(expected = IllegalArgumentException.class)
    public void testNonExistentFile() {
        TaskOne task = new TaskOne();
        task.executeCommands("cat non_existent_file.txt");
    }

   @Test(expected = IllegalArgumentException.class)
    public void testInvalidCommand() throws IOException {
        TaskOne task = new TaskOne();
        Files.write(Paths.get("test.txt"), List.of("test"));
        task.executeCommands("invalid test.txt");
    }
}
