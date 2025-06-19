package org.jdta.growapp.Controllers;

import javafx.concurrent.Task;
import javafx.fxml.Initializable;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.control.ScrollPane;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import java.io.IOException;
import java.util.Objects;
import java.util.List;
import java.util.ArrayList;


public class PhotoController implements Initializable {

    public ScrollPane scrl_pane;
    public GridPane grid_pane;


    public ProgressIndicator loader_spinner;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Подгрузка изображений from -  \src\main\resources\Photos
        //loadImagesFromResources(); jar method need to be edited
        loadImagesFromDisk();
    }
    //Рекурсивный сбор всех изображений
    private void findImagesRecursively(File photoDir, List<File> imageFiles) {
        File[] files = photoDir.listFiles();
        if (files == null) return;

        for (File file : files) {
            if (file.isDirectory()) {
                findImagesRecursively(file, imageFiles);
            }
            else if (file.getName().matches("(?i).*\\.(png|jpg|jpeg)$")) {
                imageFiles.add(file);
            }
        }
    }

    private void loadImagesFromDisk() {

        loader_spinner.setVisible(true); // show spiner over scrll_pane

        Task<List<File>> loadTask = getListTask();

        new Thread(loadTask).start();// запускаем в фоновом потоке

/*
        File photoDir = new File("Photos");
        if (!photoDir.exists() || !photoDir.isDirectory()) {
            System.err.println(" Папка Photos не найдена");
            return;
        }
 Получаем все изображения во всех подпапках
        List<File> imageFiles = new ArrayList<>();
        findImagesRecursively(photoDir, imageFiles);

        grid_pane.getChildren().clear(); // очистить перед повторной загрузкой

        int column = 0;
        int row = 0;

        for (File imgFile : imageFiles) {
            Image image = new Image(imgFile.toURI().toString());
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(150);
            imageView.setFitHeight(135);
            imageView.setPreserveRatio(true);

            grid_pane.add(imageView, column, row);
            column++;
            if (column == 2) {
                column = 0;
                row++;
            }
        }
    }
    private Task<List<File>> getLoadTask() {
        Task<List<File>> loadTask = new Task<List<File>>() {
            @Override
            protected List<File> call()  {
                File photoDir = new File("Photos");
                List<File> imageFiles = new ArrayList<>();
                if (photoDir.exists()) {
                    findImagesRecursively(photoDir, imageFiles);
                }
                return imageFiles;
            }
        };
  return loadTask;
*/
    }

    private Task<List<File>> getListTask() {
        Task<List<File>> loadTask = new Task<>() { // getLoadTask();
            @Override
            protected List<File> call()  {
                File photoDir = new File("Photos");
                List<File> imageFiles = new ArrayList<>();
                if (photoDir.exists()) {
                    findImagesRecursively(photoDir, imageFiles);
                }
                return imageFiles;
            }
        };

        loadTask.setOnSucceeded(event -> {
            List<File> images = loadTask.getValue();
            grid_pane.getChildren().clear();

            int column = 0;
            int row = 0;

            for (File imgFile : images) {
                Image image = new Image((imgFile.toURI().toString()));
                ImageView imageView = new ImageView(image);
                imageView.setFitWidth(150);;
                imageView.setFitHeight(135);
                imageView.setPreserveRatio(true);

                grid_pane.add(imageView, column, row);
                column++;
                if (column == 2) {
                    column = 0;
                    row++;
                }
            }
            loader_spinner.setVisible(false); // скрываем спиннер
        });

        loadTask.setOnFailed(e -> {
            loader_spinner.setVisible(false); // скрыть даже при ошибке
            System.out.println("Ошибка загрузки изображений: " + loadTask.getException());
        });
        return loadTask;
    }

    /*
 jar method need to be edited
    private void loadImagesFromResources() {
        try {
            // Получаем все файлы из папки Photos
            URL folderURL = getClass().getResource("/Photos/");
            if (folderURL == null) {
                System.err.println(" Папка /Photos/ не найдена!");
                return;
            }

            // Считываем пути всех изображений внутри папки Photos
            List<String> imagePaths = new ArrayList<>();
            for (String path : Objects.requireNonNull(getClass().getClassLoader().getResources("Photos").nextElement().getFile().split("\n"))) {
                if (path.endsWith(".png") || path.endsWith(".jpg") || path.endsWith(".jpeg")) {
                    imagePaths.add("/Photos/" + path);
                }
            }

            // Заполняем GridPane
            int column = 0;
            int row = 0;
            for (String path : imagePaths) {
                Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream(path)));
                ImageView imageView = new ImageView(image);
                imageView.setFitWidth(150);
                imageView.setFitHeight(135);
                imageView.setPreserveRatio(true);
                grid_pane.add(imageView, column, row);
                column++;
                if (column == 2) {
                    column = 0;
                    row++;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
*/
}
