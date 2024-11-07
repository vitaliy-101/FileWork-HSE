package org.example;

import org.example.exceptions.FileIsNullException;
import org.example.exceptions.InputFileNameException;
import org.example.utils.MessageCreator;
import org.example.utils.MessageType;

import java.io.*;
import java.util.*;

public class FileFounder {
    private static final String SEARCH_DIRECTORY_NAME = "src/main/resources/files/";
    private static final String RESULT_DIRECTORY_NAME = "src/main/resources/result/";
    private static final File SEARCH_DIRECTORY = new File(SEARCH_DIRECTORY_NAME);
    private static final File RESULT_DIRECTORY = new File(RESULT_DIRECTORY_NAME);

    public void searchFile() {
        var in = new Scanner(System.in);
        var findFile = getFindFile(in);
        createResultWork(in, findFile);
    }

    private File getFindFile(Scanner in) {
        MessageCreator.sendMessage("Все достпные файла находятся в директории проекта: src/main/resources/files\n" +
                "Вам нужно ввести имя файла вместе с расширением, например test.txt!", MessageType.NEXT);

        while (true) {
            try {
                var fileName = getUserFileName(in);
                return validateFile(findFile(FileFounder.SEARCH_DIRECTORY, fileName));
            }
            catch (Exception e) {
                MessageCreator.sendMessage("Вы получили ошибку: \n" + e + "\nПопробуйте снова!", MessageType.NEXT);
            }
        }
    }

    private void createResultWork(Scanner in, File findFile) {
        MessageCreator.sendMessage("Введите имя файла, в котором будет лежать конечная статистика!\n" +
                "Имя файла не должно содержать расширений, тк файл записывается в формате txt!\n" +
                "Результирующие файлы лежат в директории src/main/resources/result. Если файла не существует, то создается новый.\n" +
                "В нем будет информация о количестве всех символов, встретившихся в файле", MessageType.NEXT);
        while (true) {
            try {
                var fileName = getUserFileName(in);
                var resultFile = getResultFile(fileName);
                writeResults(countCharacters(findFile), resultFile.getPath());
                break;
            } catch (Exception e) {
                MessageCreator.sendMessage("Вы получили ошибку: \n" + e + "\nПопробуйте снова!", MessageType.NEXT);
            }
        }
    }


    private File getResultFile(String fileName) {
        if (findFile(RESULT_DIRECTORY, fileName) == null) {
            return createResultFile(fileName);
        }
        return findFile(RESULT_DIRECTORY, fileName);
    }

    private  Map<Character, Integer> countCharacters(File file) throws IOException {
        var symbolCount = new HashMap<Character, Integer>();
        var fileReader = new FileReader(file);
        int c;
        while ((c = fileReader.read()) != -1) {
            char character = (char) c;
            if (Character.isAlphabetic(character) || !Character.isLetter(character)) {
                symbolCount.put(character, symbolCount.getOrDefault(character, 0) + 1);
            }
        }
        fileReader.close();
        return symbolCount;
    }

    private void writeResults(Map<Character, Integer> symbolCount, String outputPath) throws IOException {
        var writer = new BufferedWriter(new FileWriter(outputPath));
        for (var entry : symbolCount.entrySet()) {
            writer.write(entry.getKey() + ": " + entry.getValue());
            writer.newLine();
        }
        writer.close();
    }

    private String getUserFileName(Scanner in) {
        MessageCreator.sendMessage("Введите имя файла: ", MessageType.NEXT);
        return validateUserFileName(in.nextLine());
    }

    private File validateFile(File file) {
        if (file == null) {
            throw new FileIsNullException();
        }
        return file;
    }

    private File createResultFile(String fileName) {
        var resultFile = new File(RESULT_DIRECTORY_NAME + fileName + ".txt");
        try {
            resultFile.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return resultFile;
    }

    private String validateUserFileName(String fileName) {
        if (fileName.isEmpty()) {
            throw new InputFileNameException();
        }
        return fileName;
    }

    private File findFile(File directory, String fileName) {
        for (var file : Objects.requireNonNull(directory.listFiles())) {
            if (file.isDirectory()) {
                return findFile(file, fileName);
            }
            if (file.getName().equals(fileName)) {
                return file;
            }
        }
        return null;
    }
}
