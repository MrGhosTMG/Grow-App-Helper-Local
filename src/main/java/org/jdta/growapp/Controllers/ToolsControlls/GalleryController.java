package org.jdta.growapp.Controllers.ToolsControlls;

import javafx.concurrent.Task;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.jdta.growapp.DTO.Cycle;
import org.jdta.growapp.DTO.Photo;
import org.jdta.growapp.Models.Model;
import org.jdta.growapp.Utils.DeleteDialogController;
import org.jdta.growapp.Utils.FXMLUtils;
import org.jdta.growapp.Utils.PreferencesUtils;
import org.jdta.growapp.Utils.StageActions;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class GalleryController implements Initializable {

    public ScrollPane scrl_pane;
    private File photoDir;
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
        initPhotoDir();
        loadImagesFromDisc();
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

    private void onAddPhoto() {
        loadImagesFromDisc();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Browse photo");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
        File selectedFile = fileChooser.showOpenDialog(getStage());
        if (selectedFile != null) {
            try {
                if (!photoDir.exists()) {
                    photoDir.mkdirs();
                }
                File destinationFile = new File(photoDir, selectedFile.getName());
                Files.copy(selectedFile.toPath(), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                loadImagesFromDisc(); // previewRefresh
            }catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void onDeleteAllPhoto() {
        if (!photoDir.exists()) return;
        if (PreferencesUtils.isSkipDeleteConfirm()) {
            deleteAllImages();
            return;
        }
        Node root = FXMLUtils.loadWithControllerCallBackActions("/FXML/utils/Delete.fxml",
                (DeleteDialogController controller) -> {
                    controller.setMessage("Sure delete all? ");
                    controller.setOnDeleteConfirmed(() -> {
                        deleteAllImages();
                        if (controller.skipConfirmation()) {
                            PreferencesUtils.setKeySkipDeleteConfirm(true);
                        }
                    });
                });
        StageActions.showAnyDialogWithConfirm(root, "Delete Confirmation", getStage());
    }

    private void deleteAllImages() {
        loader_spinner.setVisible(true);
        //loadImagesFromDisc();
        Task<Void> delTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                File[] files = photoDir.listFiles();
                if (files != null) {
                    for (File file : files) {
                        file.delete();
                    }
                }
                return null;
            }
        };
        delTask.setOnSucceeded(e -> {
            loader_spinner.setVisible(false);
            loadImagesFromDisc();
        });
        delTask.setOnFailed(e -> {
            loader_spinner.setVisible(false);
            System.out.println("Delete failed: " + delTask.getException());
        });
        new Thread(delTask).start();
    }

    private void deletePhoto() {
        // If Don't ask selected Remove without confirmation
        if (selectedImageFile != null && selectedImageFile.exists()) {
            if (PreferencesUtils.isSkipDeleteConfirm()) {
                selectedImageFile.delete();
                selectedImageFile = null;
                selectedImageView = null;
                loadImagesFromDisc();
                PreferencesUtils.cleanAllPunfp();
                return;
            }

            // Show confirm dialog
            Node root = FXMLUtils.loadWithControllerCallBackActions("/FXML/utils/Delete.fxml",
                    (DeleteDialogController controller) -> {
                        controller.setMessage("Delete this epic photo?");
                        controller.setOnDeleteConfirmed(() -> {
                            boolean deleted = selectedImageFile.delete();
                            if (deleted) {
                                System.out.println("Deleted: " + selectedImageFile.getName());
                                selectedImageFile = null;
                                selectedImageView = null;
                                loadImagesFromDisc(); // refresh
                            } else {
                                System.out.println("Failed to delete: " + selectedImageFile.getName());
                            }
                        });
                    });
            StageActions.showAnyDialogWithConfirm(root, "Confirm Delete", getStage());
        } else {
            System.out.println("No image selected");
        }
    }

    // загрузка изображений
    private void loadImagesFromDisc() {
        loader_spinner.setVisible(true);
        grid_pane.getChildren().clear();

        Task<List<ImageView>> loadTask = new Task<>() {
            @Override
            protected List<ImageView> call() {
                List<ImageView> imageViews = new ArrayList<>();
                File[] imageFiles = photoDir.listFiles((dir, name) ->
                        name.toLowerCase().endsWith(".png") ||
                                name.toLowerCase().endsWith(".jpg") ||
                                name.toLowerCase().endsWith(".jpeg"));

                if (imageFiles != null) {
                    for (File img : imageFiles) {
                        Image image = new Image(img.toURI().toString());
                        ImageView imageView = new ImageView(image);
                        clickInit(imageView);

                        imageView.setFitWidth(120);
                        imageView.setFitHeight(120);
                        imageView.setPreserveRatio(true);
                        imageView.setCache(true);

                        // нельзя в фоне делать setOnMouseClicked — позже в UI потоке
                        imageView.setUserData(img); // временно
                        imageViews.add(imageView);
                    }
                }
                return imageViews;
            }
        };

        // результат — в FX Application Thread
        loadTask.setOnSucceeded(e -> {
            List<ImageView> imageViews = loadTask.getValue();

            int column = 0;
            int row = 0;

            for (ImageView imageView : imageViews) {
                File imgFile = (File) imageView.getUserData(); // вернуть из userData
                imageView.setOnMouseClicked(ev -> {
                    selectedImageFile = imgFile;
                    System.out.println("File to delete: " + selectedImageFile.getName());
                    imageView.setStyle("-fx-effect: dropshadow(three-pass-box, red, 10, 0, 0, 0);");

                    if (selectedImageView != null && selectedImageView != imageView) {
                        selectedImageView.setStyle(""); // сброс старого выделения
                    }
                    selectedImageView = imageView;
                });

                grid_pane.add(imageView, column, row);
                column++;
                if (column == 2) {
                    column = 0;
                    row++;
                }
            }

            loader_spinner.setVisible(false);
        });

        loadTask.setOnFailed(e -> {
            loader_spinner.setVisible(false);
            System.out.println("Ошибка загрузки: " + loadTask.getException());
        });

        new Thread(loadTask).start();
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
    // static photo directory path initialize method
    private void initPhotoDir() {
        Cycle current = Model.getInstance().getCurrentCycle();
        if (current == null) {
            System.out.println("Cycle not selected!");
            photoDir = new File("Photos/cycle_photos/_unknown");
        }
        photoDir = new File("Photos/cycle_photos/Cycle_" + current.getId());
    }
}

