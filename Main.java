import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите\nфамилия: ");
        String surname = scanner.nextLine();
        System.out.println("имя: ");
        String name = scanner.nextLine();
        System.out.println("отчество: ");
        String patronymic = scanner.nextLine();
        System.out.println("дата рождения в формате дд.мм.гггг: ");
        String dateOfBirth = scanner.nextLine();
        System.out.println("номер телефона (11 цифр): ");
        String phoneNumber = scanner.nextLine();
        System.out.println("пол (f - женщина, m - мужчина): ");
        String sex = scanner.nextLine();

        List<String> dates = new ArrayList<>();
        dates.add(surname);
        dates.add(name);
        dates.add(patronymic);
        dates.add(dateOfBirth);
        dates.add(phoneNumber);
        dates.add(sex);

        String[] namesOfLine = new String[] {"фамилия", "имя", "отчество", "дата рождения", "номер телефона", "пол"};

        int codeResult = normalize(dates, namesOfLine);
        switch (codeResult) {
            case -2 -> System.out.println("В номере телефона не хватает цифр");
            case -3 -> System.out.println("В номере телефона много цифр");
            case -4 -> System.out.println("Пол содержит несколько символов");
            case -5 -> System.out.println("Такого пола не существует");
            default -> {
                createNewFile(dates);
            }
        }

        try {
            isNumeric(dates, namesOfLine);
        }
        catch (NumberFormatException e) {
            System.out.println(e.getMessage());
        }

        try {
            birthDayFormat(dateOfBirth);
        }
        catch (ParseException e) {
            System.out.println(e.getMessage());
        }

        try {
            checkPhoneNumber(phoneNumber);
        }
        catch (NumberFormatException e) {
            System.out.println(e.getMessage());
        }
    }

    static void checkSize(List<String> dates, String[] strings){
        int i = 0;
        for (String s : dates) {
            if (s.isEmpty()) System.out.println("Строка пустая \"" + strings[i] + "\"");
            i++;
        }

        int j = 0;
        for (String s : dates.subList(0, 3)) {
            if (s.length() < 2) {
                System.out.println(strings[j] + " меньше двух символов");
                j++;
            }
        }
    }

    static int normalize(List<String> dates, String[] strings) {
        checkSize(dates, strings);
        if (dates.get(4).length() > 0 && dates.get(4).length() < 11) {
            return -2;
        }
        if (dates.get(4).length() > 11) {
            return -3;
        }
        if (dates.get(5).length() > 1) {
            return -4;
        }
        if (!dates.get(5).equals("f") & !dates.get(5).equals("m")) {
            return -5;
        }
        return dates.size();
    }

    static void isNumeric(List<String> dates, String[] str)  {
        int i = 0;
        for (String s : dates.subList(0, 3)) {
            try {
                if (s.matches(".*\\d+.*")){
                    throw new NumberFormatException();
                }

            } catch (NumberFormatException e) {
                System.out.println("ФИО должно сожержать только буквы, а " + str[i] + " содержит число");
            }
            i++;
        }
    }


    static void checkPhoneNumber(String phoneNumber) throws NumberFormatException {
        if (!phoneNumber.isEmpty()) {
            try {
                Integer.parseInt(phoneNumber);
            } catch (NumberFormatException e) {
                System.out.println("Неверный формат номера телефона");
            }
        }
    }


    static void birthDayFormat(String dateOfBirth) throws ParseException {
        if (!dateOfBirth.isEmpty()) {
            try {
                SimpleDateFormat format = new SimpleDateFormat("d.M.y");
                Date date = format.parse(dateOfBirth);
            } catch (ParseException e) {
                throw new ParseException("Неверно веден день рождения", e.getErrorOffset());
            }
        }
    }


    static void createNewFile(List<String> dates) {
        String fileName = dates.get(0);
        try (FileWriter fileWriter = new FileWriter(Paths.get(fileName).toFile(), true)) {
            BufferedReader br = new BufferedReader(new FileReader(fileName));
            br.readLine();
            fileWriter.append("\n");
            for (var i : dates) {
                fileWriter.write("<" + i + ">");
                fileWriter.write(" ");
            }
        } catch (IOException e){
            System.err.println("Файл не создан, в данных есть ошибки");
            e.printStackTrace();
        }
    }
}
