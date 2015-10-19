package db61b;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
/**import java.util.Arrays;*/
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

/**import java.util.Set;*/
import static db61b.Utils.*;

/** A single table in a database.
 *  @author P. N. Hilfinger
 */
class Table implements Iterable<Row> {
    /** A new Table whose columns are given by COLUMNTITLES, which may
     *  not contain duplicate names. */
    private String[] columntitles;

    /**A table constructor who has parameter COLUMNTITLES. */
    Table(String[] columnTitles) {
        for (int i = columnTitles.length - 1; i >= 1; i -= 1) {
            for (int j = i - 1; j >= 0; j -= 1) {
                if (columnTitles[i].equals(columnTitles[j])) {
                    throw error("duplicate column name: %s",
                                columnTitles[i]);
                }
            }
        }

        this.columntitles = columnTitles;
    }


    /** Returns titles of the columns of this table as a String. */
    public String getcolumnNames() {
        String titles = new String();
        String title;
        String div = " ";
        for (int i = 0; i < this.columns(); i++) {
            title = getTitle(i);
            titles += div + title;
            div = ",";
            titles = titles.trim();

        }
        return titles;
    }


    /** A new Table whose columns are give by COLUMNTITLES. */
    Table(List<String> columnTitles) {
        this(columnTitles.toArray(new String[columnTitles.size()]));
    }

    /** Return the number of columns in this table. */
    public int columns() {
        return columntitles.length;
    }

    /** Return the title of the Kth column.  Requires 0 <= K < columns(). */
    public String getTitle(int k) {
        if (0 <= k && k < columns()) {
            return columntitles[k];
        }
        return null;
    }

    /** Return the number of the column whose title is TITLE, or -1 if
     *  there isn't one. */
    public int findColumn(String title) {
        for (int i = 0; i < columntitles.length; i++) {
            if ((columntitles[i]).equals(title)) {
                return i;
            }
        }
        return -1;
    }

    /** Return the number of Rows in this table. */
    public int size() {
        return _rows.size();
    }

    /** Returns an iterator that returns my rows in an unspecfied order. */
    @Override
    public Iterator<Row> iterator() {
        return _rows.iterator();
    }

    /** Add ROW to THIS if no equal row already exists.  Return true if anything
     *  was added, false otherwise. */
    public boolean add(Row row) {
        if (row.size() == this.columns()) {
            return _rows.add(row);
        }  else {
            throw error("Number of columns is not the same");
        }
    }

    /** Read the contents of the file NAME.db, and return as a Table.
     *  Format errors in the .db file cause a DBException. */
    static Table readTable(String name) {
        BufferedReader input;
        Table table;
        input = null;
        table = null;
        try {
            input = new BufferedReader(new FileReader(name + ".db"));

            String header = input.readLine();
            if (header == null) {
                throw error("missing header in DB file");
            }
            String[] columnNames = header.split(",");

            table = new Table(columnNames);

            String header1 = input.readLine();

            while (header1 != null) {
	      	String[] rowcont = header1.split(",");
		Row a = new Row(rowcont);
		table.add(a);
		header1 = input.readLine();

	    }

        } catch (FileNotFoundException e) {
            throw error("could not find %s.db", name);
        } catch (IOException e) {
            throw error("problem reading from %s.db", name);
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    /* Ignore IOException */
                }
            }
        }
        return table;
    }

    /** Write the contents of TABLE into the file NAME.db. Any I/O errors
     * cause a DBException.
     * iterate throught the array
     * convert each row into a string calling arraytoString method or something
     * then add it to sep and add a new line which is on piazza ADD A NEW LINE
     */
    void writeTable(String name) {
        PrintStream output;
        output = null;
        try {
            String sep;
            sep = " ";
            output = new PrintStream(name + ".db");

            Iterator str = iterator();
            output.append(getcolumnNames() + "\r\n");
            while (str.hasNext()) {
                Row a = (Row) str.next();
                String b = a.toString();

                output.append(b + sep + "\r\n");
            }

        } catch (IOException e) {
            throw error("trouble writing to %s.db", name);
        } finally {
            if (output != null) {
                output.close();
            }
        }
    }

    /** Print my contents on the standard output. */
    void print() {
        for (Row a : _rows) {
            System.out.print("  ");
            System.out.println(a.toString2());
        }
    }

    /** Return a new Table whose columns are COLUMNNAMES, selected from
     *  rows of this table that satisfy CONDITIONS. */
    Table select(List<String> columnNames, List<Condition> conditions) {
        Table result = new Table(columnNames);

        for (Row x: _rows) {
            int counter = 0;
            String[] res = new String[columnNames.size()];

            for (String y : columnNames) {
                Column a = new Column(y, this);
                String val = a.getFrom(x);
                res[counter] = val;
                counter += 1;

            }

            if (Condition.test(conditions, x)) {
                Row newrow = new Row(res);
                result.add(newrow);
            }
        }

        return result;
    }

    /** Return a new Table whose columns are COLUMNNAMES, selected
     *  from pairs of rows from this table and from TABLE2 that match
     *  on all columns with identical names and satisfy CONDITIONS. */
    Table select(Table table2, List<String> columnNames,
                 List<Condition> conditions) {
        Table result = new Table(columnNames);

        List<String> comName = new ArrayList<String>();
        List<Column> col1 = new ArrayList<Column>();
        List<Column> col2 = new ArrayList<Column>();

        for (int a = 0; a < this.columns(); a++) {
            for (int b = 0; b < table2.columns(); b++) {
                if (this.getTitle(a).equals(table2.getTitle(b))) {
                    comName.add(this.getTitle(a));

                }
            }
        }
        for (int i = 0;  i < comName.size(); i++) {
            col1.add(new Column(comName.get(i), this));
            col2.add(new Column(comName.get(i), table2));
        }

        List<Column> colnames = new ArrayList<Column>();

        for (int index = 0; index < columnNames.size(); index++) {
            Column c = new Column(columnNames.get(index), this, table2);
            colnames.add(c);
        }


        if (comName.size() != 0) {
            for (Row row1 : this._rows) {
                for (Row row2 : table2._rows) {
                    if (Table.equijoin(col1, col2, row1, row2)
                        && Condition.test(conditions, row1, row2)) {
                        Row bigrow = new Row(colnames, row1, row2);
                        result.add(bigrow);
                    }
                }
            }
        } else {
            for (Row row1 : this._rows) {
                for (Row row2 : table2._rows) {
                    if (Condition.test(conditions, row1, row2)) {
                        Row bigrow = new Row(colnames, row1, row2);
                        result.add(bigrow);
                    }
                }
            }
        }
        return result;
    }

    /** Return true if the columns COMMON1 from ROW1 and COMMON2 from
     *  ROW2 all have identical values.  Assumes that COMMON1 and
     *  COMMON2 have the same number of elements and the same names,
     *  that the columns in COMMON1 apply to this table, those in
     *  COMMON2 to another, and that ROW1 and ROW2 come, respectively,
     *  from those tables. */
    public static boolean equijoin(List<Column> common1,
                                   List<Column> common2, Row row1, Row row2) {

        for (int i = 0; i < common1.size(); i++) {
            String comp = common2.get(i).getFrom(row2);
            if (!common1.get(i).getFrom(row1).equals(comp)) {
                return false;
            }
        }
        return true;
    }

    /** My rows. */
    private HashSet<Row> _rows = new HashSet<>();
}

