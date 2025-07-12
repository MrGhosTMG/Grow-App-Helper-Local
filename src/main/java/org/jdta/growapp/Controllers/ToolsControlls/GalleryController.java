package org.jdta.growapp.Controllers.ToolsControlls;

import javafx.concurrent.Task;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.jdta.growapp.DTO.Photo;
import org.jdta.growapp.Models.Model;
import org.jdta.growapp.Utils.DeleteDialogController;
import org.jdta.growapp.Utils.FXMLUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class GalleryController implements Initializable {

    private static final File PHOTO_DIR = new File("Photos/cycle_photos/Cycle_" +
            Model.getInstance().getCurrentCycle().getId());
    private File selectedImageFile;
    private ImageView selectedImageView;

    public Button add_photo_btn;
    public Button delete_photo_btn;
    public StackPane stack_pane;
    public GridPane grid_pane;
    public ProgressIndicator loader_spinner;
    public AnchorPane parent_anchor;
    public Button del_all;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadImagesFromDisk();
        add_photo_btn.setOnAction(actionEvent -> onAddPhoto());
        delete_photo_btn.setOnAction(actionEvent -> deletePhoto());
        del_all.setOnAction(actionEvent -> onDeleteAllPhoto());
    }
    private File checkCreatePhotosDir() {
        Photo photo = new Photo();
        String path = "cycle" + photo.getCycleId();
        File photosDir = new File(path);
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
                clickInit(imageView);
                imageView.setPreserveRatio(true);
                imageView.setFitWidth(150);
                imageView.setFitHeight(135);
                imageView.setSmooth(true);
                imageView.setCache(true);

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

    private void onAddPhoto() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Browse photo");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
        File selectedFile = fileChooser.showOpenDialog(getStage());
        if (selectedFile != null) {
            try {
                if (!PHOTO_DIR.exists()) {
                    PHOTO_DIR.mkdirs();
                }
                File destinationFile = new File(PHOTO_DIR, selectedFile.getName());
                Files.copy(selectedFile.toPath(), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                loadImagesFromTemp(); // previewRefresh
            }catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void onDeleteAllPhoto() {
        if (PHOTO_DIR.exists()) {
            for (File file : PHOTO_DIR.listFiles()) {
                file.delete();
            }
        }
    }

    private void deletePhoto() {
        if (selectedImageFile != null && selectedImageFile.exists()) {
            // Show confirm dialog
            Node root = FXMLUtils.loadWithControllerCallBackActions("/FXML/utils/DeleteDialog.fxml",
                    (DeleteDialogController controller) -> {
                        controller.setMessage("Delete this epic photo?");
                        controller.setOnDeleteConfirmed(() -> {
                            boolean deleted = selectedImageFile.delete();
                            if (deleted) {
                                System.out.println("Deleted: " + selectedImageFile.getName());
                                selectedImageFile = null;
                                selectedImageView = null;
                                loadImagesFromDisk(); // refresh
                            } else {
                                System.out.println("Failed to delete: " + selectedImageFile.getName());
                            }
                        });
                    });
            FXMLUtils.showDialogStage(root, "Confirm Delete", getStage()); // no showDialogStage()
        } else {
            System.out.println("No image selected");
        }
    }
    private void clearTempFolder() {
        if (PHOTO_DIR.exists()) {
            for (File file : PHOTO_DIR.listFiles()) {
                file.delete();
            }
            loadImagesFromTemp();
        }
    }
    private void loadImagesFromTemp() {
        File[] imageFiles = PHOTO_DIR.listFiles(((dir, name) ->
                name.toLowerCase().endsWith(".png") ||
                        name.toLowerCase().endsWith(".jpg") ||
                        name.toLowerCase().endsWith(".jpeg")));
        grid_pane.getChildren().clear();

        if (imageFiles == null) return;
        int column = 0;
        int row = 0;

        for (File img : imageFiles) {
            Image image = new Image(img.toURI().toString());
            ImageView imageView = new ImageView(image);
            clickInit(imageView);
            imageView.setFitWidth(150);
            imageView.setFitHeight(135);
            imageView.setPreserveRatio(true);

            grid_pane.add(imageView, column, row);
            column++;
            if (column == 2) {
                column = 0;
                row ++;
            }
        }
    }
    private Stage getStage() {
        return FXMLUtils.stageFrom(add_photo_btn); // можно использовать любой доступный Node
    }

    private void clickInit(ImageView imageView) {
        imageView.setOnMouseClicked(e -> {
            if (selectedImageView != null) {
                selectedImageView.setStyle("");
            }
            selectedImageView = imageView;
            String url = imageView.getImage().getUrl(); // file:/.../Photos/...
            if (url.startsWith("file:/")) {
                selectedImageFile = new File(url.replace("file:/", "").replace("%20", " "));
            }
            //selectedImageFile = selectedImageFile;
            imageView.setStyle("-fx-effect: dropshadow(gaussian, red, 10, 0.5, 0, 0);");
        });
    }
}

