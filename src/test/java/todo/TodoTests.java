package todo;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.testng.AssertJUnit.assertEquals;

public class TodoTests {
    WebDriver driver;

    @BeforeClass
    public void setup() {
        System.setProperty("webdriver.chrome.driver", "F:\\seleniumprojects\\TestTodolist\\drivers\\chromedriver\\chromedriver.exe"); // adjust path
        driver = new ChromeDriver();
        //driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        driver.manage().window().maximize();
        driver.get("https://todomvc.com/examples/react/dist/");
    }
//TC1-Add list and count the items
    @Test(priority = 1)
    public void addTodoTest() {
        //Add
        WebElement input = driver.findElement(By.cssSelector("input[placeholder='What needs to be done?']"));
        input.sendKeys("Buy groceries");
        input.sendKeys(Keys.ENTER);

        List<WebElement> todos = driver.findElements(By.cssSelector(".todo-list li"));
        Assert.assertEquals(todos.size(), 1);
        Assert.assertTrue(todos.get(0).getText().contains("Buy groceries"));
        WebElement itemLeft = driver.findElement(By.xpath("//*[contains(text(),'1 item left')]"));
        assert itemLeft.getText().contains("1 item left");
    }

//Tc2-Mark Todo as Complete
  @Test(priority = 2)
    public void completeTodoTest() {

      WebElement todoItem = driver.findElement(By.cssSelector(".todo-list li"));
      WebElement toggleCheckbox = todoItem.findElement(By.cssSelector("input.toggle"));


      toggleCheckbox.click();

      // Verify the 'completed' class is applied to the todo item
      String classAttribute = todoItem.getAttribute("class");
      Assert.assertTrue(classAttribute.contains("completed"), "Todo item should have class 'completed' after being checked");
     System.out.println("Class attribute: " + classAttribute);
      Assert.assertTrue(classAttribute.contains("completed"), "Todo item should have class 'completed' after being checked");
    }
    //TC3-Filter to show only completed Todos
    @Test(priority = 3)
    public void testCompletedFilterShowsOnlyCompletedTodos() throws InterruptedException {

        WebElement input = driver.findElement(By.cssSelector("input[placeholder='What needs to be done?']"));

        // Add 2 todos
        input.sendKeys("Buy Milk");
        input.sendKeys(Keys.RETURN);
        input.sendKeys("Buy Food");
        input.sendKeys(Keys.RETURN);

        // Mark the first todo as completed
        List<WebElement> todos = driver.findElements(By.cssSelector(".todo-list li"));
        WebElement firstTodoCheckbox = todos.get(1).findElement(By.cssSelector("input.toggle"));
        firstTodoCheckbox.click();

        // Click the "Completed" filter button
        WebElement completedFilter = driver.findElement(By.linkText("Completed"));
        completedFilter.click();

        // Now only completed tasks should be visible
        List<WebElement> visibleTodos = driver.findElements(By.cssSelector(".todo-list li"));
        Assert.assertEquals(visibleTodos.size(), 2, "Only one completed todo should be visible.");
    Thread.sleep(100);
       // WebElement itemLeft = driver.findElement(By.xpath("//*[contains(text(),'2 item left')]"));
        //assert itemLeft.getText().contains("2 item left");
        String classAttr = visibleTodos.get(0).getAttribute("class");
        Assert.assertTrue(classAttr.contains("completed"), "Visible todo should be marked as completed.");
        String classAttr1 = visibleTodos.get(1).getAttribute("class");
        Assert.assertTrue(classAttr1.contains("completed"), "Visible todo should be marked as completed.");
    }
    //TC4-Filter to show All Todos
    @Test(priority = 4)
    public void AllTodos() {



        WebElement allFilter = driver.findElement(By.linkText("All"));
        allFilter.click();
        List<WebElement> visibleTodos = driver.findElements(By.cssSelector(".todo-list li"));
        // Now only completed tasks should be visible

        Assert.assertEquals(visibleTodos.size(), 3, "Both Active and  completed todo should be visible.");

    }
    //TC5-Filter to show Active Todos
    @Test(priority = 5)
    public void ActiveTodos() {



        WebElement allFilter = driver.findElement(By.linkText("Active"));
        allFilter.click();
        List<WebElement> visibleTodos = driver.findElements(By.cssSelector(".todo-list li"));
        // Now only completed tasks should be visible

        Assert.assertEquals(visibleTodos.size(), 1, "Only Active Todo's should be visible.");
        String classAttr = visibleTodos.get(0).getAttribute("class");
        Assert.assertFalse(classAttr.contains("completed"), "Visible todo should not be marked as completed.");

    }
    //TC6-Filter to Clear Completed Todos by clicking Clear completed button
    @Test(priority = 6)
    public void ClearCompletedTodos() {
        WebElement allFilter = driver.findElement(By.linkText("All"));
        allFilter.click();
        List<WebElement> visibleTodos = driver.findElements(By.cssSelector(".todo-list li"));
        Assert.assertEquals(visibleTodos.size(), 3, "Only one completed todo should be visible.");
        WebElement clearcompleted = driver.findElement(By.className("clear-completed"));
        clearcompleted.click();
        allFilter.click();
        List<WebElement> remainingTodos = driver.findElements(By.cssSelector(".todo-list li"));

        // Verify only 1 todo remains
        Assert.assertEquals(remainingTodos.size(), 1, "Only one active todo should remain after clearing completed.");
        //Assert.assertEquals(visibleTodos.size(), 1,"All completed should be removed");
    }
    //TC7-Select All Todos by clicking AllToggleTodos
    @Test(priority = 7)
    public void AllToggleTodos() {
        WebElement allFilter = driver.findElement(By.linkText("All"));
        allFilter.click();
       WebElement toggleall = driver.findElement(By.className("toggle-all"));
        toggleall.click();
        List<WebElement> visibleTodos = driver.findElements(By.cssSelector(".todo-list li"));
        Assert.assertEquals(visibleTodos.size(), 1, "Only one completed todo should be visible.");
        String classAttr1 = visibleTodos.get(0).getAttribute("class");
        Assert.assertTrue(classAttr1.contains("completed"), "Visible todo should be marked as completed.");
        //WebElement clearcompleted = driver.findElement(By.className("clear-completed"));
       // clearcompleted.click();



    }
    //TC8-UnSelect All Todos by clicking AllToggleTodos again
    @Test(priority = 8)
    public void AllToggleunselectTodos() {
        WebElement allFilter = driver.findElement(By.linkText("All"));
        allFilter.click();
        WebElement toggleall = driver.findElement(By.className("toggle-all"));
        toggleall.click();
        List<WebElement> visibleTodos = driver.findElements(By.cssSelector(".todo-list li"));
        Assert.assertEquals(visibleTodos.size(), 1, "Only one completed todo should be visible.");
        String classAttr1 = visibleTodos.get(0).getAttribute("class");
        Assert.assertFalse(classAttr1.contains("completed"), "Visible todo should be marked as completed.");

    }
    //TC9-Remove Todos by clicking X button
    @Test(priority = 9)
    public void RemoveTodos() {
        WebElement newTodo = driver.findElement(By.className("new-todo"));
        newTodo.sendKeys("Task to Delete");
        newTodo.sendKeys(Keys.ENTER);

        // Step 2: Wait for the todo item to appear
        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".todo-list li")));

        // Step 3: Hover over the <div class="view"> to reveal the X button
        WebElement viewDiv = driver.findElement(By.cssSelector(".todo-list li .view"));
        Actions actions = new Actions(driver);
        actions.moveToElement(viewDiv).perform();

        // Step 4: Click the destroy (X) button
        WebElement destroyButton = driver.findElement(By.cssSelector(".todo-list li .destroy"));
        destroyButton.click();

        // Step 5: Confirm the item is deleted (list should be empty)
        List<WebElement> todos = driver.findElements(By.cssSelector(".todo-list li"));
        assertEquals(1, todos.size());
    }
    //TC10-Editing Todos by double clicking Todos
    @Test(priority = 10)
    public void testDoubleClickToEditTodo() throws InterruptedException {


        WebDriverWait wait = new WebDriverWait(driver, 5);
        WebElement label = driver.findElement(By.cssSelector("li .view label"));
        Actions actions = new Actions(driver);
        actions.doubleClick(label).perform();
        Thread.sleep(100);
        WebElement input1 = driver.findElement(By.className("view"));
        System.out.println(input1.getAttribute("innerHTML"));
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("document.getElementById('todo-input').value = arguments[0];", "New Task");
        List<WebElement> todos = driver.findElements(By.cssSelector(".todo-list li"));
        assertEquals(1, todos.size());
        Assert.assertFalse(todos.get(0).getText().contains("Task to Delete"));

    }

    @AfterClass
    public void tearDown() {
        driver.quit();
    }
}