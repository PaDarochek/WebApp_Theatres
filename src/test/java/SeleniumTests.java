import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.*;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated;
import static org.testng.Assert.*;

public class SeleniumTests {

    String URL = "http://localhost:8080/";
    WebDriver driver = new ChromeDriver();
    WebDriverWait wait;

    @BeforeClass
    public void setup() {
        wait = new WebDriverWait(driver,10);
        driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
        driver.manage().window().maximize();
        System.setProperty("webdriver.chrome.driver", "/usr/lib/chromium-browser/chromedriver");
    }

    @Test
    public void testMainPage() {
        URL = "http://localhost:8080/";
        driver.get(URL);
        WebElement mainTable = wait.until(visibilityOfElementLocated(By.className("main-table")));
        List<WebElement> mainTableRows = mainTable.findElements(By.tagName("tr"));
        Timestamp curDate = new Timestamp(System.currentTimeMillis());
        assertTrue(mainTableRows.stream().map(r -> r.findElements(By.tagName("td")).stream()
                .map(WebElement::getText)
                .collect(Collectors.toList()).get(1)).map(dt -> {
            try {
                return dt != null && (new Timestamp
                (new SimpleDateFormat("dd.MM.yyyy HH:mm").parse(dt).getTime()).after(curDate));
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }).allMatch(c -> true));
    }

    @Test
    public void testTheatresPage() {
        URL = "http://localhost:8080/theatres";
        driver.get(URL);
        WebElement mainTable = wait.until(visibilityOfElementLocated(By.className("main-table")));
        List<WebElement> mainTableRows = mainTable.findElements(By.tagName("tr"));
        assertEquals(mainTableRows.size(), 3);

        WebElement nextBtn = driver.findElement(By.id("button-next"));
        nextBtn.click();
        mainTable = wait.until(visibilityOfElementLocated(By.className("main-table")));
        mainTableRows = mainTable.findElements(By.tagName("tr"));
        assertEquals(mainTableRows.size(), 2);
        WebElement prevBtn = driver.findElement(By.id("button-prev"));
        prevBtn.click();

        prevBtn = driver.findElement(By.id("button-prev"));
        prevBtn.click();
        mainTable = wait.until(visibilityOfElementLocated(By.className("main-table")));
        mainTableRows = mainTable.findElements(By.tagName("tr"));
        assertEquals(mainTableRows.size(), 3);

        WebElement nameFilter = driver.findElement(By.name("name"));
        nameFilter.sendKeys("Чехова");
        WebElement findBtn = driver.findElement(By.id("find"));
        findBtn.click();
        mainTable = wait.until(visibilityOfElementLocated(By.className("main-table")));
        mainTableRows = mainTable.findElements(By.tagName("tr"));
        assertEquals(mainTableRows.size(), 1);
        WebElement td = mainTableRows.get(0).findElements(By.tagName("td")).get(0);
        assertEquals(td.getText(), "Московский Художественный театр имени А.П. Чехова");
    }

    @Test
    public void testPerformancesPage() {
        URL = "http://localhost:8080/performances";
        driver.get(URL);
        WebElement mainTable = wait.until(visibilityOfElementLocated(By.className("main-table")));
        List<WebElement> mainTableRows = mainTable.findElements(By.tagName("tr"));

        for (Integer i = 0; i < 3; ++i) {
            assertEquals(mainTableRows.size(), 3);
            WebElement nextBtn = driver.findElement(By.id("button-next"));
            nextBtn.click();
            mainTable = wait.until(visibilityOfElementLocated(By.className("main-table")));
            mainTableRows = mainTable.findElements(By.tagName("tr"));
        }
        assertEquals(mainTableRows.size(), 0);

        for (Integer i = 0; i < 4; ++i) {
            WebElement prevBtn = driver.findElement(By.id("button-prev"));
            prevBtn.click();
            mainTable = wait.until(visibilityOfElementLocated(By.className("main-table")));
            mainTableRows = mainTable.findElements(By.tagName("tr"));
            assertEquals(mainTableRows.size(), 3);
        }

        WebElement nameFilter = driver.findElement(By.name("theatre"));
        nameFilter.sendKeys("Эстрады");
        WebElement directorFilter = driver.findElement(By.name("director"));
        directorFilter.sendKeys("Михаил Фоменко");
        WebElement actorsFilter = driver.findElement(By.name("actors"));
        actorsFilter.sendKeys("Василий Дахненко, Дарья Шевчук");
        WebElement startDateFilter = driver.findElement(By.name("start_date"));
        startDateFilter.sendKeys("2022-05-15");
        WebElement endDateFilter = driver.findElement(By.name("end_date"));
        endDateFilter.sendKeys("2022-05-21");

        WebElement findBtn = driver.findElement(By.id("find"));
        findBtn.click();
        mainTable = wait.until(visibilityOfElementLocated(By.className("main-table")));
        mainTableRows = mainTable.findElements(By.tagName("tr"));
        assertEquals(mainTableRows.size(), 1);
        WebElement td = mainTableRows.get(0).findElements(By.tagName("td")).get(0);
        assertEquals(td.getText(), "Моя бедная крыша\nМосковский Государственный Театр Эстрады");
    }

    @Test
    public void testPerformancePage() {
        URL = "http://localhost:8080/performances";
        driver.get(URL);
        WebElement mainTable = wait.until(visibilityOfElementLocated(By.className("main-table")));
        List<WebElement> mainTableRows = mainTable.findElements(By.tagName("tr"));
        WebElement td = mainTableRows.get(2).findElements(By.tagName("td")).get(0);
        String title = td.getText().split("\n")[0];
        WebElement perf = td.findElement(By.tagName("a"));
        perf.click();
        assertEquals(driver.getTitle(), title);

        WebElement session = driver.findElements(By.id("date_time")).get(0);
        List<String> date_time = List.of(session.getText().split(" "));
        WebElement buyBtn = driver.findElement(By.id("buy_button"));
        buyBtn.click();
        mainTable = wait.until(visibilityOfElementLocated(By.className("main-table")));
        mainTableRows = mainTable.findElements(By.tagName("tr"));
        td = mainTableRows.get(0).findElements(By.tagName("td")).get(0);
        List<String> text = List.of(td.getText().split("\n"));
        assertEquals(text.get(2), "Дата: " + date_time.get(0));
        assertEquals(text.get(3), "Время: " + date_time.get(1));
    }

    @Test
    public void testTheatrePage() {
        URL = "http://localhost:8080/theatres";
        driver.get(URL);
        WebElement mainTable = wait.until(visibilityOfElementLocated(By.className("main-table")));
        List<WebElement> mainTableRows = mainTable.findElements(By.tagName("tr"));
        WebElement td = mainTableRows.get(1).findElements(By.tagName("td")).get(0);
        String title = td.getText();
        WebElement theatre = td.findElement(By.tagName("a"));
        theatre.click();
        assertEquals(driver.getTitle(), title);

        WebElement sessionsTable = wait.until(visibilityOfElementLocated(By.id("sessions_table")));
        List<WebElement> sessionsTableRows = sessionsTable.findElements(By.tagName("tr"));
        sessionsTableRows.forEach(s ->
        {
            String curURL = driver.getCurrentUrl();
            WebElement perf = s.findElements(By.tagName("td")).get(0);
            String perfName = perf.getText().split("\n")[0];
            WebElement buyBtn = s.findElements(By.tagName("td")).get(1).findElement(By.tagName("a"));
            buyBtn.click();
            assertEquals(driver.getTitle(), "Выбрать билет на " + perfName);

            WebElement table = wait.until(visibilityOfElementLocated(By.className("main-table")));
            List<WebElement> tableRows = table.findElements(By.tagName("tr"));
            WebElement cell = tableRows.get(0).findElements(By.tagName("td")).get(0).findElement(By.tagName("a"));
            cell.click();

            table = wait.until(visibilityOfElementLocated(By.className("main-table")));
            tableRows = table.findElements(By.tagName("tr"));
            cell = tableRows.get(0).findElements(By.tagName("td")).get(0);
            List<String> text = List.of(cell.getText().split("\n"));
            assertEquals(text.get(0), "Театр: " + title);
            driver.get(curURL);
        });

        WebElement filterBtn = driver.findElement(By.id("filter_button"));
        filterBtn.click();
        mainTable = wait.until(visibilityOfElementLocated(By.className("main-table")));
        mainTableRows = mainTable.findElements(By.tagName("tr"));
        mainTableRows.forEach(r ->
        {
            WebElement cell = r.findElements(By.tagName("td")).get(0);
            String theatreName = cell.getText().split("\n")[1];
            assertEquals(theatreName, title);
        });
    }

    @Test
    public void testChooseBuyTicketsPages() {
        URL = "http://localhost:8080/performances";
        driver.get(URL);
        WebElement mainTable = wait.until(visibilityOfElementLocated(By.className("main-table")));
        List<WebElement> mainTableRows = mainTable.findElements(By.tagName("tr"));
        WebElement td = mainTableRows.get(2).findElements(By.tagName("td")).get(0);
        String title = td.getText().split("\n")[0];

        WebElement perf = td.findElement(By.tagName("a"));
        perf.click();
        WebElement session = driver.findElements(By.id("date_time")).get(0);
        List<String> date_time = List.of(session.getText().split(" "));

        WebElement buyBtn = driver.findElement(By.id("buy_button"));
        buyBtn.click();
        Select category = new Select(driver.findElement(By.id("category")));
        category.selectByValue("mezzanine");
        WebElement categoryBtn = driver.findElement(By.id("category_button"));
        categoryBtn.click();

        Select row = new Select(driver.findElement(By.id("row")));
        row.selectByValue("3");
        WebElement rowBtn = driver.findElement(By.id("row_button"));
        rowBtn.click();

        Select seat = new Select(driver.findElement(By.id("seat")));
        seat.selectByValue("17");
        WebElement seatBtn = driver.findElement(By.id("seat_button"));
        seatBtn.click();

        mainTable = wait.until(visibilityOfElementLocated(By.className("main-table")));
        mainTableRows = mainTable.findElements(By.tagName("tr"));
        td = mainTableRows.get(0).findElements(By.tagName("td")).get(0);
        List<String> ticket = List.of(td.getText().split("\n"));
        assertEquals(ticket.get(0), title);
        assertEquals(ticket.get(2), "Дата: " + date_time.get(0));
        assertEquals(ticket.get(3), "Время: " + date_time.get(1));
        assertEquals(ticket.get(5), "Категория: бельэтаж");
        assertEquals(ticket.get(6), "Ряд: 3");
        assertEquals(ticket.get(7), "Место: 17");
    }

    @Test
    public void testAuthorize() {
        URL = "http://localhost:8080/";
        driver.get(URL);
        WebElement authBtn = driver.findElement(By.id("auth_button"));
        authBtn.click();

        WebElement login = driver.findElement(By.id("login"));
        login.sendKeys("admin_1");
        WebElement password = driver.findElement(By.id("password"));
        password.sendKeys("iwFBAL");
        WebElement findBtn = driver.findElement(By.id("find"));
        findBtn.click();

        assertEquals(driver.getTitle(), "Театральная касса");
        try {
            driver.findElement(By.id("auth_button"));
        } catch (Exception e) {
            assertEquals(e.getClass(), NoSuchElementException.class);
        }
    }

    @Test
    public void testAddUpdateDeleteTheatre() {
        URL = "http://localhost:8080/";
        driver.get(URL);
        WebElement authBtn = driver.findElement(By.id("auth_button"));
        authBtn.click();

        WebElement login = driver.findElement(By.id("login"));
        login.sendKeys("admin_1");
        WebElement password = driver.findElement(By.id("password"));
        password.sendKeys("iwFBAL");
        WebElement findBtn = driver.findElement(By.id("find"));
        findBtn.click();

        WebElement thBtn = driver.findElement(By.id("theatres_button"));
        thBtn.click();
        WebElement addThBtn = driver.findElement(By.id("add_theatre"));
        addThBtn.click();

        WebElement name = driver.findElement(By.id("name"));
        name.sendKeys("Мой театр");
        WebElement address = driver.findElement(By.id("address"));
        address.sendKeys("Москва, Ломоносовский пр., д. 17");
        WebElement gr_fl_rows = driver.findElement(By.id("gr_fl_rows"));
        gr_fl_rows.sendKeys("10");
        WebElement gr_fl_row_seats = driver.findElement(By.id("gr_fl_seats"));
        gr_fl_row_seats.sendKeys("20");
        WebElement mez_rows = driver.findElement(By.id("mez_rows"));
        mez_rows.sendKeys("5");
        WebElement mez_row_seats = driver.findElement(By.id("mez_seats"));
        mez_row_seats.sendKeys("25");
        WebElement bal_rows = driver.findElement(By.id("bal_rows"));
        bal_rows.sendKeys("7");
        WebElement bal_row_seats = driver.findElement(By.id("bal_seats"));
        bal_row_seats.sendKeys("30");

        WebElement addBtn = driver.findElement(By.id("add_button"));
        addBtn.click();

        thBtn = driver.findElement(By.id("theatres_button"));
        thBtn.click();
        WebElement nextBtn = driver.findElement(By.id("button-next"));
        nextBtn.click();
        WebElement mainTable = wait.until(visibilityOfElementLocated(By.className("main-table")));
        List<WebElement> mainTableRows = mainTable.findElements(By.tagName("tr"));
        WebElement theatre = mainTableRows.get(2).findElement(By.tagName("td"));
        assertEquals(theatre.getText(), "Мой театр");
        thBtn = theatre.findElement(By.tagName("a"));
        thBtn.click();

        WebElement updateBtn = driver.findElement(By.id("update_theatre"));
        updateBtn.click();
        WebElement gr_fl_text = driver.findElement(By.id("gr_fl_text"));
        assertEquals(gr_fl_text.getText(), "Количество мест в ряду в партере: 20");
        gr_fl_row_seats = driver.findElement(By.id("gr_fl_seats"));
        gr_fl_row_seats.sendKeys("23");
        WebElement grFlBtn = driver.findElement(By.id("gr_fl_button"));
        grFlBtn.click();
        gr_fl_text = driver.findElement(By.id("gr_fl_text"));
        assertEquals(gr_fl_text.getText(), "Количество мест в ряду в партере: 23");

        thBtn = driver.findElement(By.id("theatres_button"));
        thBtn.click();
        nextBtn = driver.findElement(By.id("button-next"));
        nextBtn.click();
        mainTable = wait.until(visibilityOfElementLocated(By.className("main-table")));
        mainTableRows = mainTable.findElements(By.tagName("tr"));
        theatre = mainTableRows.get(2).findElement(By.tagName("td"));
        assertEquals(theatre.getText(), "Мой театр");
        thBtn = theatre.findElement(By.tagName("a"));
        thBtn.click();

        WebElement deleteThBtn = driver.findElement(By.id("delete_theatre"));
        deleteThBtn.click();
        thBtn = driver.findElement(By.id("theatres_button"));
        thBtn.click();
        mainTable = wait.until(visibilityOfElementLocated(By.className("main-table")));
        mainTableRows = mainTable.findElements(By.tagName("tr"));
        mainTableRows.forEach(r ->
        {
            WebElement th = r.findElement(By.tagName("td"));
            assertNotEquals(th.getText(), "Мой театр");
        });

        nextBtn = driver.findElement(By.id("button-next"));
        nextBtn.click();
        mainTable = wait.until(visibilityOfElementLocated(By.className("main-table")));
        mainTableRows = mainTable.findElements(By.tagName("tr"));
        mainTableRows.forEach(r ->
        {
            WebElement th = r.findElement(By.tagName("td"));
            assertNotEquals(th.getText(), "Мой театр");
        });
    }

    @Test
    public void testAddUpdateDeletePerformance() {
        URL = "http://localhost:8080/";
        driver.get(URL);
        WebElement authBtn = driver.findElement(By.id("auth_button"));
        authBtn.click();

        WebElement login = driver.findElement(By.id("login"));
        login.sendKeys("admin_1");
        WebElement password = driver.findElement(By.id("password"));
        password.sendKeys("iwFBAL");
        WebElement findBtn = driver.findElement(By.id("find"));
        findBtn.click();

        WebElement perfBtn = driver.findElement(By.id("performances_button"));
        perfBtn.click();
        WebElement addPerfBtn = driver.findElement(By.id("add_performance"));
        addPerfBtn.click();

        WebElement name = driver.findElement(By.id("name"));
        name.sendKeys("Мой спектакль");
        WebElement theatreName = driver.findElement(By.id("theatre_name"));
        theatreName.sendKeys("Государственный академический Малый театр");
        WebElement duration = driver.findElement(By.id("duration"));
        duration.sendKeys("02:15");
        WebElement director = driver.findElement(By.id("director"));
        director.sendKeys("Ольга Карасёва");

        WebElement addBtn = driver.findElement(By.id("add_button"));
        addBtn.click();

        perfBtn = driver.findElement(By.id("performances_button"));
        perfBtn.click();
        WebElement nextBtn = driver.findElement(By.id("button-next"));
        nextBtn.click();
        nextBtn = driver.findElement(By.id("button-next"));
        nextBtn.click();
        nextBtn = driver.findElement(By.id("button-next"));
        nextBtn.click();
        WebElement mainTable = wait.until(visibilityOfElementLocated(By.className("main-table")));
        List<WebElement> mainTableRows = mainTable.findElements(By.tagName("tr"));
        WebElement perf = mainTableRows.get(0).findElements(By.tagName("td")).get(0);
        assertEquals(perf.getText().split("\n")[0], "Мой спектакль");
        assertEquals(perf.getText().split("\n")[1], "Государственный академический Малый театр");
        perfBtn = perf.findElement(By.tagName("a"));
        perfBtn.click();

        WebElement updateBtn = driver.findElement(By.id("update_performance"));
        updateBtn.click();
        WebElement actor = driver.findElement(By.id("actor"));
        actor.sendKeys("Марина Девятова");
        WebElement actorRole = driver.findElement(By.id("actor_role"));
        actorRole.sendKeys("Жюли");
        WebElement actorBtn = driver.findElement(By.id("add_actor_button"));
        actorBtn.click();

        mainTable = wait.until(visibilityOfElementLocated(By.className("main-table")));
        mainTableRows = mainTable.findElements(By.tagName("tr"));
        WebElement actorText = mainTableRows.get(3).findElement(By.tagName("td")).findElement(By.tagName("div"));
        assertEquals(actorText.getText(), "Марина Девятова: Жюли Удалить");

        actor = driver.findElement(By.id("actor"));
        actor.sendKeys("Александр Добрынин");
        actorRole = driver.findElement(By.id("actor_role"));
        actorRole.sendKeys("Пьер");
        actorBtn = driver.findElement(By.id("add_actor_button"));
        actorBtn.click();

        mainTable = wait.until(visibilityOfElementLocated(By.className("main-table")));
        mainTableRows = mainTable.findElements(By.tagName("tr"));
        List<WebElement> actorTexts = mainTableRows.get(3).findElement(By.tagName("td")).findElements(By.tagName("div"));
        String name1 = "Марина Девятова: Жюли Удалить";
        String name2 = "Александр Добрынин: Пьер Удалить";
        assertTrue(actorTexts.get(0).getText().equals(name1) ||
                actorTexts.get(0).getText().equals(name2));
        assertTrue(actorTexts.get(1).getText().equals(name1) ||
                actorTexts.get(1).getText().equals(name2));

        mainTable = wait.until(visibilityOfElementLocated(By.className("main-table")));
        mainTableRows = mainTable.findElements(By.tagName("tr"));
        List<WebElement> actors = mainTableRows.get(3).findElement(By.tagName("td")).findElements(By.tagName("div"));
        String restName = actors.get(0).getText().equals(name1) ? name2 : name1;
        WebElement delActorBtn = actors.get(0).findElement(By.tagName("a"));
        delActorBtn.click();

        mainTable = wait.until(visibilityOfElementLocated(By.className("main-table")));
        mainTableRows = mainTable.findElements(By.tagName("tr"));
        actorTexts = mainTableRows.get(3).findElement(By.tagName("td")).findElements(By.tagName("div"));
        assertTrue(actorTexts.get(0).getText().equals(restName));

        WebElement session_date_time = driver.findElement(By.id("session_date_time"));
        session_date_time.sendKeys("2022-05-10 20:00:00.0");
        WebElement session_gr_fl = driver.findElement(By.id("session_gr_fl"));
        session_gr_fl.sendKeys("2000");
        WebElement session_mez = driver.findElement(By.id("session_mez"));
        session_mez.sendKeys("1800");
        WebElement session_bal = driver.findElement(By.id("session_bal"));
        session_bal.sendKeys("1600");
        WebElement sessionBtn = driver.findElement(By.id("add_session_button"));
        sessionBtn.click();

        mainTable = wait.until(visibilityOfElementLocated(By.className("main-table")));
        mainTableRows = mainTable.findElements(By.tagName("tr"));
        String session1 = "Дата: 10.05.2022\nВремя: 20:00\nЦена билета в партер: 2000\n" +
                "Цена билета в бельэтаж: 1800\nЦена билета на балкон: 1600\nУдалить";
        String session2 = "Дата: 23.05.2022\nВремя: 20:00\nЦена билета в партер: 2000\n" +
                "Цена билета в бельэтаж: 1800\nЦена билета на балкон: 1600\nУдалить";
        WebElement sessionsText = mainTableRows.get(4).findElement(By.tagName("td")).findElement(By.tagName("div"));
        assertEquals(sessionsText.getText(), session1);

        session_date_time = driver.findElement(By.id("session_date_time"));
        session_date_time.sendKeys("2022-05-23 20:00:00.0");
        session_gr_fl = driver.findElement(By.id("session_gr_fl"));
        session_gr_fl.sendKeys("2000");
        session_mez = driver.findElement(By.id("session_mez"));
        session_mez.sendKeys("1800");
        session_bal = driver.findElement(By.id("session_bal"));
        session_bal.sendKeys("1600");
        sessionBtn = driver.findElement(By.id("add_session_button"));
        sessionBtn.click();

        mainTable = wait.until(visibilityOfElementLocated(By.className("main-table")));
        mainTableRows = mainTable.findElements(By.tagName("tr"));
        List<WebElement> sessionsTexts = mainTableRows.get(4).findElement(By.tagName("td")).findElements(By.tagName("div"));
        assertTrue(sessionsTexts.get(1).getText().equals(session1) || sessionsTexts.get(1).getText().equals(session2));
        assertTrue(sessionsTexts.get(0).getText().equals(session1) || sessionsTexts.get(0).getText().equals(session2));

        mainTable = wait.until(visibilityOfElementLocated(By.className("main-table")));
        mainTableRows = mainTable.findElements(By.tagName("tr"));
        List<WebElement> sessions = mainTableRows.get(4).findElement(By.tagName("td")).findElements(By.tagName("div"));
        String restSession = sessions.get(0).getText().equals(session1) ? session2 : session1;
        WebElement delSessionBtn = sessions.get(0).findElement(By.tagName("a"));
        delSessionBtn.click();

        mainTable = wait.until(visibilityOfElementLocated(By.className("main-table")));
        mainTableRows = mainTable.findElements(By.tagName("tr"));
        sessionsText = mainTableRows.get(4).findElement(By.tagName("td")).findElement(By.tagName("div"));
        assertEquals(sessionsText.getText(), restSession);

        perfBtn = driver.findElement(By.id("performances_button"));
        perfBtn.click();
        nextBtn = driver.findElement(By.id("button-next"));
        nextBtn.click();
        nextBtn = driver.findElement(By.id("button-next"));
        nextBtn.click();
        nextBtn = driver.findElement(By.id("button-next"));
        nextBtn.click();
        nextBtn = driver.findElement(By.id("button-next"));
        nextBtn.click();
        mainTable = wait.until(visibilityOfElementLocated(By.className("main-table")));
        mainTableRows = mainTable.findElements(By.tagName("tr"));
        perf = mainTableRows.get(0).findElements(By.tagName("td")).get(0);
        assertEquals(perf.getText().split("\n")[0], "Мой спектакль");
        perfBtn = perf.findElement(By.tagName("a"));
        perfBtn.click();

        WebElement deletePerfBtn = driver.findElement(By.id("delete_performance"));
        deletePerfBtn.click();
        perfBtn = driver.findElement(By.id("performances_button"));
        perfBtn.click();
        mainTable = wait.until(visibilityOfElementLocated(By.className("main-table")));
        mainTableRows = mainTable.findElements(By.tagName("tr"));
        mainTableRows.forEach(r ->
        {
            WebElement p = r.findElements(By.tagName("td")).get(0);
            assertNotEquals(p.getText().split("\n")[0], "Мой спектакль");
        });

        nextBtn = driver.findElement(By.id("button-next"));
        nextBtn.click();
        mainTable = wait.until(visibilityOfElementLocated(By.className("main-table")));
        mainTableRows = mainTable.findElements(By.tagName("tr"));
        mainTableRows.forEach(r ->
        {
            WebElement th = r.findElement(By.tagName("td"));
            assertNotEquals(th.getText(), "Мой театр");
        });

        nextBtn = driver.findElement(By.id("button-next"));
        nextBtn.click();
        mainTable = wait.until(visibilityOfElementLocated(By.className("main-table")));
        mainTableRows = mainTable.findElements(By.tagName("tr"));
        mainTableRows.forEach(r ->
        {
            WebElement th = r.findElement(By.tagName("td"));
            assertNotEquals(th.getText(), "Мой театр");
        });
    }
}