package Lab4;

public class CalendarPrinter {
    private static final int DAYS_IN_WEEK = 7;
    private static final int MONTH_SPACING = 20;
    private static final int[] DAYS_PER_MONTH = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
    private static final int [] DAYS_PER_MONTH_LEAP = {31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

    private static final String DAYS_FORMAT = "Su Mo Tu We Th Fr Sa   ";
    private static final String[] MONTHS = {"January", "February", "March", "April", "May", "June", "July", "August",
            "September", "October", "November", "December"};

    private static String holidays(int month, int weekday, int dayNumber) {

        /* https://stackoverflow.com/questions/5762491/how-to-print-color-in-console-using-system-out-println
        This prints out the holidays in red. */
        String ANSI_RESET = "\u001B[0m";
        String ANSI_RED = "\u001B[31m";

        /* New Year's Day */
        if (month == 1 && dayNumber == 1) {
            return (ANSI_RED + "*y" + ANSI_RESET);
        }

        /* President's Day */
        else if (month == 2 && weekday == 1 && (dayNumber >= 15 && dayNumber <= 21)) {
            return (ANSI_RED + "*p" + ANSI_RESET);
        }

        /* Normal Tax Day */
        else if (month == 4 && (weekday != 0 && weekday != 6) && dayNumber == 15) {
            return (ANSI_RED + "*x" + ANSI_RESET);
        }

        /* Tax Day on Weekend */
        else if (month == 4 && weekday == 1 && (dayNumber == 16 || dayNumber == 17)) {
            return (ANSI_RED + "*x" + ANSI_RESET);
        }

        /* Memorial Day */
        else if (month == 5 && weekday == 1 && (dayNumber >= 25 && dayNumber <= 31)) {
            return (ANSI_RED + "*m" + ANSI_RESET);
        }

        /* 4th of July */
        else if (month == 7 && dayNumber == 4) {
            return (ANSI_RED + "*4" + ANSI_RESET);
        }

        /* Labor Day */
        else if (month == 9 && weekday == 1 && dayNumber <= 7) {
            return (ANSI_RED + "*L" + ANSI_RESET);
        }

        /* Veteran's Day */
        else if (month == 11 && dayNumber == 11) {
            return (ANSI_RED + "*v" + ANSI_RESET);
        }

        /* Thanksgiving */
        else if (month == 11 && weekday == 4 && (dayNumber >= 22 && dayNumber <= 28)) {
            return (ANSI_RED + "*t" + ANSI_RESET);
        }

        /* Christmas */
        else if (month == 12 && dayNumber == 25) {
            return (ANSI_RED + "*c" + ANSI_RESET);
        } else if (dayNumber < 10) {
            return " " + dayNumber;
        } else {
            return "" + dayNumber;
        }
    }

    /* Function used to determine first day of each month */
    private static int firstOfMonth(int janFirst, int month, boolean leapYear) {
        for (int i = 1; i < month; i++) {
            janFirst = (janFirst + DAYS_PER_MONTH[i - 1]) % DAYS_IN_WEEK;
            if (leapYear && i == 2) {
                janFirst = (janFirst + 1) % DAYS_IN_WEEK;
            }
        }
        return janFirst;
    }

    /* Returns the heading for the months for the getYear function */
    private static String monthHeading(int month) {
        String headerLine;
        String whiteSpace = "";
        String margin = "   ";

        if (month == 7) {
            headerLine = "";
        } else {
            headerLine = " ";
        }

        /* Loop's index starts at -1 so the heading of each month gets printed in sets of three */
        for (int i = -1; i <= 1; i++) {
            for (int j = 0; j < (MONTH_SPACING - MONTHS[month + i].length()) / 2; j++) {
                whiteSpace += " ";
            }
            headerLine += (whiteSpace + MONTHS[month + i] + whiteSpace + margin);
            whiteSpace = "";
        }
        return (headerLine + "\n" + DAYS_FORMAT+ DAYS_FORMAT + DAYS_FORMAT);
    }

    private static String getWeek (boolean blankLine, int month, int weekday, int dayNumber, boolean leapYear,
                                    boolean holiday){
        String week = "";
        int date = dayNumber;
        boolean endOfMonth = false;
        if (blankLine) {
            return "                     ";
        }

        for (int i = 0; i < DAYS_IN_WEEK; i++) {
            /* Fills in the number of blank days in the beginning of the month */
            if (dayNumber == 1 && i < weekday) {
                week += "  ";
            }
            else {
                if (!endOfMonth){
                    if (holiday){
                        week += holidays(month, weekday, date);
                        weekday = (weekday + 1) % DAYS_IN_WEEK;
                    }
                    else {
                        if (date >= 10){
                            week += date;
                        }
                        else {
                            week += " " + date;
                        }
                    }

                    if (leapYear){
                        if (month == 2){
                            if (date >= DAYS_PER_MONTH[month - 1] + 1){
                                endOfMonth = true;
                            }
                        }
                        else if (date >= DAYS_PER_MONTH[month - 1]){
                            endOfMonth = true;
                        }
                    }
                    else if (date >= DAYS_PER_MONTH[month - 1]){
                        endOfMonth = true;
                    }
                }

                else {
                    week += "  ";
                }
                date++;
            }
            week += " ";
        }
        return week;
    }

    private static String[][] getMonth (int year, int month, int janFirst, boolean leapYear){
        String [][] monthOutput = new String[DAYS_IN_WEEK][DAYS_IN_WEEK];
        int[] months = DAYS_PER_MONTH;
        int firstDay = firstOfMonth(janFirst, month, leapYear);
        int date = 1;
        int row = 1;

        if (leapYear) {
            months = DAYS_PER_MONTH_LEAP;
        }

        /* Formats the Month Heading */
        for (int i = 0; i < (MONTH_SPACING - (MONTHS[month - 1].length() + 1 + (year + "").length())) / 2; i++) {
            System.out.print(" ");
        }
        System.out.println(MONTHS[month - 1] + " " + year);
        System.out.println(DAYS_FORMAT);

        for (int i = firstDay; i < DAYS_IN_WEEK ; i++) {
            monthOutput[0][i] = String.format("%2d ", date);
            date++;
        }

        for (int i = date; i <= months[month - 1]; i+= 7) {
            for (int j = 0; j < DAYS_IN_WEEK; j++) {
                monthOutput[row][j] = String.format("%2d ", date);
                date++;

                if (date > months[month -1]){
                    break;
                }
            }
            row++;
        }

        for (int i = 0; i < DAYS_IN_WEEK; i++) {
            for (int j = 0; j < DAYS_IN_WEEK; j++) {
                if (monthOutput[i][j] == null){
                    monthOutput[i][j] = "   ";
                }
            }
        }

        return monthOutput;
    }

    private void getYear (int year, int janFirst, boolean leapYear){
        String yearOutput;
        String margin = "  ";
        int yearSpacing = 67;
        int month = 1;

        /* Calender gets printed out in a table of four rows and three columns */
        int monthsPerRow = 3;
        int rows = 4;

        for (int i = 0; i < (yearSpacing - ("" + year).length())/2; i++) {
            System.out.print(" ");
        }
        System.out.println(year);

        for (int i = 0; i < rows; i++) {
            boolean [] endOfMonth = {false, false, false};
            int[][] dates = {{1, 1, 1}, {firstOfMonth(janFirst, month, leapYear),
                    firstOfMonth(janFirst, month + 1, leapYear),
                    firstOfMonth(janFirst, month + 2, leapYear)}};

            System.out.println(monthHeading(month));

            for (int j = 0; j < DAYS_IN_WEEK - 1 ; j++) {
                /* To print out each week of every month, in sets of three */
                yearOutput = getWeek(endOfMonth[0], month, dates[1][0], dates[0][0], leapYear, true)
                        + margin + getWeek(endOfMonth[1], month + 1, dates[1][1], dates[0][1], leapYear,
                        true) + margin
                        + getWeek(endOfMonth[2], month + 2, dates[1][2], dates[0][2], leapYear,
                        true);

                for (int k = 0; k < monthsPerRow ; k++) {
                    if (dates[0][k] != 1){
                        dates[0][k] += DAYS_IN_WEEK;
                    }
                    else {
                        dates[0][k] += DAYS_IN_WEEK - dates[1][k];
                    }
                    dates[1][k] = 0;
                    if (leapYear){
                        if (month == 2){
                            if (dates[0][k] > DAYS_PER_MONTH[month - 1 + k] + 1){
                                endOfMonth[k] = true;
                            }
                        }
                        else if (dates[0][k] > DAYS_PER_MONTH[month - 1 + k]){
                            endOfMonth[k] = true;
                        }
                    }
                    else if (dates[0][k] > DAYS_PER_MONTH[month - 1 + k]) {
                        endOfMonth[k] = true;
                    }
                }
                System.out.println(yearOutput);
            }
            month += 3;
        }
    }

    private void printMonth (int year, int month, int janFirst, boolean leapYear) {
        String [][] monthOutput = getMonth(year, month, janFirst, leapYear);
        for (int i = 0; i < DAYS_IN_WEEK; i++) {
            for (int j = 0; j < DAYS_IN_WEEK; j++) {
                System.out.print(monthOutput[i][j]);
            }
            System.out.println();
        }
    }

    private void printYear (int year, int janFirst, boolean leapYear){
        CalendarPrinter printer = new CalendarPrinter();
        printer.getYear(year, janFirst, leapYear);
    }
}
