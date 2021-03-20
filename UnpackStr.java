import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UnpackStr {

    // Создаем три разных стека для чисел, для скобок и для букв
    Stack<Integer> numbers = new Stack<>();
    Stack<Character> brackets = new Stack<>();
    Stack<String> letters = new Stack<>();

    // Метод - распаковка входной строки
    public String unpack(String input) throws Exception {

        // Проверка на валидность вводной строки
        Pattern valid = Pattern.compile("^[a-z0-9\\[\\]]{1,30}$");
        Matcher isValid = valid.matcher(input);
        if (!isValid.matches())
            throw new Exception("Invalid input string!");

        // Создаем переменные для результата и временной строки
        StringBuilder result = new StringBuilder();
        StringBuilder letter = new StringBuilder();

        // Прохождим циклом по вводной строке и проверяем каждый символ и добавляем в соответствующий стек
        for (int j = 0; j < input.length(); j++) {
            char ch = input.charAt(j);

            if (ch >= '0' && ch <= '9')
                numbers.push(ch - '0');

            else if (ch == '[')
                brackets.push(ch);

            // Особый случай закрывающаяся првая скобка - проводим необходимые операции
            else if (ch == ']') {
                letters.push(letter.toString());

                // обнуляем временную переменную-хранилище последовательности букв
                letter = new StringBuilder();

                if (!brackets.empty()) {
                    if (brackets.size() != 1) {
                        // используем временный стек для реверса временной строки
                        StringBuilder tmp = new StringBuilder();
                        Stack<String> lettersTemp = new Stack<>();
                        while (!letters.empty()) {
                            lettersTemp.push(letters.pop());
                        }
                        while (!lettersTemp.empty()) {
                            tmp.append(lettersTemp.pop());
                        }
                        brackets.pop();
                        letters.push(repeatStr(numbers.pop(), tmp.toString()));
                    } else {
                        brackets.pop();
                        letters.push(repeatStr(numbers.pop(), letters.pop()));
                    }

                    // Достаем и переворачиваем результирующую строку с помощью другого стека
                    Stack<String> tempRes = new Stack<>();
                    if (numbers.empty()) {
                        while (!letters.empty()) {
                            tempRes.push(letters.pop());
                        }
                        while (!tempRes.empty()) {
                            result.append(tempRes.pop());
                        }
                    }

                } else
                    // Проверка на валидность открывающихся и закрывающихся скобок
                    System.out.println("Error: " + ch +" at "+ j +" not have a couple");

            // если стек скобок пуст - добавляем символ в результат, иначе во временную переменную
            } else if (!brackets.empty())
                letter.append(ch);
            else
                result.append(ch);
        }
        return result.toString();
    }

    // вспомогательный метод - множитель строки
    public String repeatStr(int repeatNum, String repeatStr) {
        StringBuilder result = new StringBuilder();
        for (int i = 1; i <= repeatNum; i++)
            result.append(repeatStr);
        return result.toString();
    }

    // тестируем (пока юнит-тесты не применяю, не изучил еще подробно)
    public static void main(String[] args) throws Exception {
        System.out.println(new UnpackStr().unpack("3[xyz]4[xy]z"));
        System.out.println(new UnpackStr().unpack("2[3[x]y]"));
    }
}
