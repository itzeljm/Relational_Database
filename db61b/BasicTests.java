package db61b;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
/**Tests the basic functionality of:
 * 1. The row class
 */

public class BasicTests {
    @Test
    public void testRow() {

        Row r = new Row(new String[]{"I", "want", "to", "eat", "cookies"});
        assertEquals(5, r.size());
    }


    @Test
    public void testGet() {
        Row a = new Row(new String[]{"This", "is", "pretty", "confusing"});
        String result = "confusing";
        assertEquals(result, a.get(3));
    }


    @Test
    public void testEquals() {
        Row o = new Row(new String[]{"There", "is", "something", "weird"});
        Row p = new Row(new String[]{"There", "is", "something", "weird"});
        assertEquals(true, o.equals(p));
    }

    @Test
    public void testtoStringRow() {
        Row q = new Row(new String[]{"SID", "Year", "Birthday"});
        String result = "SID,Year,Birthday";
        System.out.println(result);
        System.out.println(q.toString());
        assertEquals(result, q.toString());
    }

    @Test
    public void testTableColumns() {
        String[] coltitles = new String[]{"books", "cookies",
                                          "food", "milkshakes", "veggies"};
        Table b = new Table(coltitles);
        int bsize = 5;
        assertEquals(bsize, b.columns());
    }

    @Test
    public void testTableGetTitle() {
        String[] column1 = new String[]{"I", "want", "a", "milkshake"};
        Table c = new Table(column1);
        assertEquals("milkshake", c.getTitle(3));
    }

    @Test
    public void testTableFindColumn() {
        String[] column2 = new String[]{"I", "want",
                                        "a", "cookie", "and", " a rocket"};
        Table d = new Table(column2);
        assertEquals(3, d.findColumn("cookie"));
        assertEquals(-1, d.findColumn("not here"));
    }

    @Test
    public void testTableSize() {
        String[] column2 = new String[]{"I", "want", "a",
                                        "cookie", "and", " a rocket"};
        Table d = new Table(column2);
        d.add(new Row(new String[] {"I", "am", "a", "turtle"}));
        d.add(new Row(new String[]{"hello", "you"}));
        assertEquals(2, d.size());
    }

    @Test
    public void testAddRow() {
        String [] column3 = new String[]{"SID", "Birthdate", "Year"};
        Table e = new Table(column3);
        assertEquals(true,  e.add(new Row(new String[]
        {"001", "09/25", "first"})));
        assertEquals(false,  e.add(new Row(new String[]
        {"001", "09/25", "first"})));
    }

    @Test
    public void testprintTable() {
        String [] columnt4 = new String[]{"SID", "Year", "Age"};
        Table m = new Table(columnt4);
        Row row1 = new Row(new String[]{"002", "first", "18"});
        Row row2 = new Row(new String[]{"003", "second", "19"});
        Row row3 = new Row(new String[]{"004", "third", "20"});
        m.add(row1);
        m.add(row2);
	m.add(row3);
        m.print();
        System.out.println(" ");

    }


    @Test
    public void testreadTable2() {
        Table a = Table.readTable("db61b/schedule");
        a.print();
        System.out.println(" ");

        Table lm = Table.readTable("db61b/enrolled");
        lm.print();
    }


    @Test
    public void testWriteTable() {
        Table b = new Table(new String[]{"SID", "year", "age"});
        b.add(new Row(new String[]{"001", "01", "01"}));
        b.add(new Row(new String[]{"002", "02", "02"}));
        b.add(new Row(new String[]{"003", "01", "22"}));
        b.add(new Row(new String[]{"111", "02", "30"}));
        b.add(new Row(new String[]{"245", "01", "40"}));
        b.add(new Row(new String[]{"273", "01", "50"}));

        b.writeTable("db61b/blank");

        Table c = new Table(new String[]{"SID", "Lastname", "Firstname",
                                         "SemEnter", "YearEnter", "Major"});
        c.add(new Row(new String[]{"101", "Knowles", "Jason",
                                   "F", "2003", "EECS"}));
        c.add(new Row(new String[]{"102", "Chan", "Jonathan",
                                   "S", "2004", "Math"}));
        c.writeTable("db61b/blank");
    }

    @Test
    public void testgetandputDataB() {
        Table another = new Table(new String[]{"SID", "Year", "Column"});
        another.add(new Row(new String[]{"152", "woles",
                                         "son", "M", "3333", "EECS"}));
        another.add(new Row(new String[]{"369", "lles", "mar",
                                         "F", "2013", "EECS"}));
        another.add(new Row(new String[]{"852", "princess", "star",
                                         "f", "4033", "EECS"}));
        Database dat = new Database();
        dat.put("students", another);
        assertEquals(another, dat.get("students"));
    }

    @Test
    public void testloadStatement() {
        String s = "load enrolled;";
        Scanner scan = new Scanner(s);
        CommandInterpreter interpreter =
            new CommandInterpreter(scan, System.out);
        interpreter.loadStatement();
    }



    @Test
    public void testRowCons() {
        String[] columns = new String[]{"SID", "Lastname", "Firstname",
                                        "SemEnter", "YearEnter", "Major"};
        Table studentsTable = new Table(columns);
        Row one = new Row(new String[]{"101", "Knowles",
                                       "Jason", "F", "2003", "EECS"});
        Row two = new Row(new String[]{"102", "Chan",
                                       "Jonathan", "S", "2004", "Math"});
        studentsTable.add(one);
        studentsTable.add(two);
        Column cL = new Column("Lastname", studentsTable);
        Column cF = new Column("Firstname", studentsTable);
        List<Column> listofcolumns = new ArrayList<Column>();
        listofcolumns.add(cL);
        listofcolumns.add(cF);
        Row r0 = new Row(listofcolumns, one);
        Row compare = new Row(new String[]{"Knowles", "Jason"});

        Table r01 = new Table(new String[]{"Lastname", "Firstname"});
        Table compare1 =  new Table(new String[]{"Lastname", "Firstname"});

        r01.add(r0);
        compare1.add(compare);
        System.out.println(" ");

        r01.print();

        System.out.println(" ");
        compare1.print();

        assertEquals(compare, r0);


    }

    @Test
    public void testselectTable() {

        Table wtv = new Table(new String[]{"SID", "Lastname", "Firstname",
                                           "SemEnter", "YearEnter", "Major"});
        wtv.add(new Row(new String[]{"101",
                                     "Knowles", "Jason", "F", "2003", "EECS"}));
        wtv.add(new Row(new String[]{"102", "Chan", "Jonathan",
                                     "S", "2004", "Math"}));

        List<String> columnNames = new ArrayList<String>();
        columnNames.add("SID");
        columnNames.add("Firstname");

        Table two = new Table(new String[]{"SID", "Lastname", "Firstname",
                                           "SemEnter",
                                           "YearEnter", "Major"});
        two.add(new Row(new String[]{"100", "Awesome",
                                     "Itzel", "F", "2009", "EECS"}));
        two.add(new Row(new String[]{"200", "Lopez",
                                     "Liz", "S", "2001", "Math"}));



        Table result1 = new Table(new String[]{"SID", "Firstname"});
        result1.add(new Row(new String[]{"101", "Jason"}));
        result1.add(new Row(new String[]{"102", "Jonathan"}));

        Table result2 = new Table(new String[]{"SID", "Firstname"});
        result2.add(new Row(new String[]{"100", "Itzel"}));
        result2.add(new Row(new String[]{"200", "Liz"}));


        List<Condition> empty = new ArrayList<Condition>();
        Table outcome =  wtv.select(columnNames, empty);
        Table outcome2 = two.select(columnNames, empty);

        System.out.println(" ");

        result1.print();
        System.out.println(" ");
        outcome.print();

        result2.print();
        System.out.println(" ");
        outcome2.print();

    }

    @Test
    public void testequijoin() {
        Table one = new Table(new String[]{"SID", "Lastname", "Firstname",
                                           "SemEnter", "YearEnter", "Major"});
        Row rowone = new Row(new String[]{"123", "Lalo", "Uziel",
                                          "F", "2003", "EECS"});
        one.add(rowone);
        one.add(new Row(new String[]{"456", "Max", "Jaden",
                                     "S", "2004", "Math"}));
        Table two = new Table(new String[]{"SID", "Lastname", "Firstname",
                                           "SemEnter", "YearEnter", "Major"});
        Row rowtwo = new Row(new String[]{"123", "Lalo", "Uziel",
                                          "F", "2003", "Business"});
        two.add(rowtwo);
        two.add(new Row(new String[]{"456", "Max", "Jaden",
                                     "S", "2004", "Math"}));

        Column col1 = new Column("Major", one);
        Column col2 = new Column("Major", two);

        List<Column> common1 = new ArrayList<Column>();
        common1.add(col1);

        List<Column> common2 = new ArrayList<Column>();
        common2.add(col2);

        assertEquals(false, Table.equijoin(common1, common2, rowone, rowtwo));
    }

    @Test
    public void testselectTable2() {


        Table a = new Table(new String[]{"SID", "Lastname", "Firstname"});
        a.add(new Row(new String[]{"111", "Martinez", "Itzel"}));
        a.add(new Row(new String[]{"222", "Lopez", "Liz"}));
        a.add(new Row(new String[]{"333", "Knowles", "Jason"}));
        a.add(new Row(new String[]{"444", "Chan", "Jonathan"}));



        System.out.println(" ");

        Table b = new Table(new String[]{"SID", "YearEnter", "Major"});
        b.add(new Row(new String[]{"555",  "2003", "EECS"}));
        b.add(new Row(new String[]{"333",  "2004", "Math"}));
        b.add(new Row(new String[]{"111",  "2013", "EECS"}));
        b.add(new Row(new String[]{"222", "2010", "Math"}));



        List<String> columnNames = new ArrayList<String>();
        columnNames.add("SID");
        columnNames.add("YearEnter");

        Table result1 = new Table(new String[]{"SID", "YearEnter"});
        result1.add(new Row(new String[]{"111", "2013"}));
        result1.add(new Row(new String[]{"222", "2010"}));

        List<Condition> empty = new ArrayList<Condition>();
        System.out.println(" ");

        Table outcome =  a.select(b, columnNames, empty);
        outcome.print();

        System.out.println(" ");



        result1.print();
        System.out.println(" ");

    }


    @Test
    public void testselect2() {
        Table a = new Table(new String[]{"SID", "Lastname", "Firstname"});
        a.add(new Row(new String[]{"111", "Martinez", "Itzel"}));
        a.add(new Row(new String[]{"222", "Lopez", "Liz"}));
        a.add(new Row(new String[]{"333", "Knowles", "Jason"}));
        a.add(new Row(new String[]{"444", "Chan", "Jonathan"}));

        System.out.println(" ");

        Table b = new Table(new String[]{"SID", "YearEnter", "Major"});
        b.add(new Row(new String[]{"555",  "2003", "EECS"}));
        b.add(new Row(new String[]{"123",  "2004", "Math"}));
        b.add(new Row(new String[]{"111",  "2013", "EECS"}));
        b.add(new Row(new String[]{"222", "2010", "Math"}));



        List<String> columnNames = new ArrayList<String>();
        columnNames.add("SID");
        columnNames.add("Firstname");
        columnNames.add("YearEnter");
        columnNames.add("Major");

        List<Condition> condition1 = new ArrayList<Condition>();

        Table outcome =  a.select(b, columnNames, condition1);
        outcome.print();
    }



    public static void main(String[] args) {
        System.exit(ucb.junit.textui.runClasses(BasicTests.class));

    }
}

