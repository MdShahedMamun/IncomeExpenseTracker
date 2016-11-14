package app.com.example.shahed.incomeexpensetracker;

import android.provider.BaseColumns;

/**
 * Created by shahed on 27-Jun-16.
 */
public class IncomeExpenseContract {
    public static String email="";

    /**
     * Inner class that defines the table contents of the the student table
     */
    public static final class ItemEntry implements BaseColumns {

        public static final String TABLE_ITEM = "item";

        public static final String COLUMN_ITEM_ID = "item_id";
        public static final String COLUMN_ITEM_NAME = "item_name";
    }

    public static final class DateEntry implements BaseColumns {

        public static final String TABLE_DATE = "date";

        public static final String COLUMN_DATE_ID = "date_id";
        public static final String COLUMN_DATE_NAME = "date_name";
    }

    public static final class ItemDateEntry implements BaseColumns {

        public static final String TABLE_ITEM_DATE = "item_date";

        public static final String COLUMN_ITEM_DATE_ID = "item_date_id";
        public static final String COLUMN_ITEM_ID = "item_id";
        public static final String COLUMN_DATE_ID = "date_id";
    }

    public static final class PersonEntry implements BaseColumns {

        public static final String TABLE_PERSON = "person";

        public static final String COLUMN_PERSON_ID = "person_id";
        public static final String COLUMN_EMAIL = "email";
        public static final String COLUMN_PASSWORD = "password";
    }

    public static final class MainEntry implements BaseColumns {

        public static final String TABLE_MAIN = "main";

        public static final String COLUMN_MAIN_ID = "main_id";
        public static final String COLUMN_ITEM_DATE_ID = "item_date_id";
        public static final String COLUMN_PERSON_ID = "person_id";
        public static final String COLUMN_AMOUNT = "amount";
        public static final String COLUMN_INCOME_EXPENSE_TYPE = "income_expense_type";
    }
}


