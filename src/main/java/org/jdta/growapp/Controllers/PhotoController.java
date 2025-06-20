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
import java.util.List;
import java.util.ArrayList;


public class PhotoController implements Initializable {

    public ScrollPane scrl_pane;
    public GridPane grid_pane;
    public ProgressIndicator loader_spinner;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadImagesFromDisk();
    }

    //создаем папку для хранения фото
    private File checkCreatePhotosDir() {
        File photosDir = new File("Photos");
        if (!photosDir.exists()) {
            boolean dirExist = photosDir.mkdirs();
            if (dirExist) {
                System.out.println("Photos directory was created");
            }
        } else {
            System.out.println("Photos directory is exists");
        }
        return photosDir;
    }

    //Рекурсивный сбор всех изображений
    private void findImagesRecursively(File photoDir, List<File> imageFiles) {
        File[] files = photoDir.listFiles();
        if (files == null) return;

        for (File file : files) {
            if (file.isDirectory()) {
                findImagesRecursively(file, imageFiles);
            } else if (file.getName().matches("(?i).*\\.(png|jpg|jpeg)$")) {
                imageFiles.add(file);
            }
        }
    }

    // получаем список задач
    private Task<List<File>> getListTask() {
        Task<List<File>> loadTask = new Task<>() { // getLoadTask();
            @Override
            protected List<File> call() {
                File photoDir = checkCreatePhotosDir();
                List<File> imageFiles = new ArrayList<>();
                if (photoDir.exists()) { findImagesRecursively(photoDir, imageFiles); }
                return imageFiles;
            }
        };
        return loadTask;
    }

    // загрузка изображений from -  \src\main\resources\Photos
    private void loadImagesFromDisk() {

        loader_spinner.setVisible(true); // show spiner over scrll_pane

        Task<List<File>> loadTask = getListTask();
        loadTask.setOnSucceeded(event -> {
            List<File> images = loadTask.getValue();
            grid_pane.getChildren().clear();

            int column = 0;
            int row = 0;

            for (File imgFile : images) {
                Image image = new Image((imgFile.toURI().toString()));
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
            loader_spinner.setVisible(false); // hide spinner
        });

        loadTask.setOnFailed(e -> {
            loader_spinner.setVisible(false); // hide spinner even if error
            System.out.println("Image load time ERROR: " + loadTask.getException());
        });
        new Thread(loadTask).start();// start in main Thread
    }
}
