package com.dan.randomvideoselector;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

/*Copyright (C) 2022  Daniel Nelson

        This program is free software: you can redistribute it and/or modify
        it under the terms of the GNU General Public License as published by
        the Free Software Foundation, either version 3 of the License, or
        (at your option) any later version.

        This program is distributed in the hope that it will be useful,
        but WITHOUT ANY WARRANTY; without even the implied warranty of
        MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
        GNU General Public License for more details.

        You should have received a copy of the GNU General Public License
        along with this program.  If not, see <https://www.gnu.org/licenses/>.
*/


//TODO:
//Make directory open button
//Add titles for text areas
//Comment code
//Make code nicer

public class Main extends Application {

    private final String APPLICATION_VERSION = "Ver 1.0";
    private final String APPLICATION_AUTHOR = "\u00A9 2022 Daniel Nelson";

    private ArrayList<File> MovieList = new ArrayList<>();

    MenuBar menuBar = new MenuBar();
    Menu fileMenu = new Menu("File");
    Menu editMenu = new Menu("Edit");

    Button RefreshButton = new Button("Randomize");
    Text videonameArea = new Text();
    Text filepathArea = new Text();

    private void buildMenus(Stage mainstage){
        //build the menus for the menu bar
        MenuItem aboutappMenuItem = new MenuItem("About App");
        fileMenu.getItems().addAll(aboutappMenuItem);
        aboutappMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Alert popupalert = new Alert(Alert.AlertType.INFORMATION);
                popupalert.setTitle("About App Dialogue");
                popupalert.setHeaderText(null);
                popupalert.setContentText(APPLICATION_VERSION + " " + APPLICATION_AUTHOR);
                popupalert.showAndWait();
            }
        });

        MenuItem guideMenuItem = new MenuItem("Guide");
        fileMenu.getItems().addAll(guideMenuItem);
        guideMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Alert guidealert = new Alert(Alert.AlertType.INFORMATION);
                guidealert.setTitle("Usage Guide");
                guidealert.setHeaderText(null);
                guidealert.setContentText("This app will randomly select a video file from the chosen directory.");
                guidealert.showAndWait();
            }
        });

        MenuItem folderMenuItem = new MenuItem("Folder");
        editMenu.getItems().addAll(folderMenuItem);
        folderMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                DirectoryChooser getTopDirectory = new DirectoryChooser();
                getTopDirectory.setInitialDirectory(new File(System.getProperty("user.home")));
                getTopDirectory.setTitle("Select Root Folder");
                File selectedDirectory = getTopDirectory.showDialog(mainstage);
                //Add all videos to ArrayList
                addVideos(selectedDirectory);
                //Start initial random video search
                selectRandomVideo(MovieList);
            }
        });
    }

    private void selectRandomVideo(ArrayList<File> movieList) {
        Random randomVideo = new Random();
        int arrayindex = randomVideo.nextInt(MovieList.size());
        File selectedvideo = MovieList.get(arrayindex);

        showVideoInfo(selectedvideo);
    }

    private void addVideos(File selectedDirectory) {
        MovieList.clear();
        getvideos(selectedDirectory, MovieList);
    }

    private void getvideos(File directory, ArrayList<File> videos){
        File listedFiles[] = directory.listFiles();
        if (listedFiles != null){
            for (File video : listedFiles){
                if (video.isFile() && (video.getName().endsWith(".m4v") || video.getName().endsWith(".mp4") || video.getName().endsWith(".mkv") || video.getName().endsWith(".avi"))){
                    videos.add(video);
                } else if (video.isDirectory()) {
                    File thisdirectory = new File(video.getAbsolutePath());
                    getvideos(thisdirectory, videos);
                } else {
                    videonameArea.setText("Directory Empty");
                }
            }
        } else {
            videonameArea.setText("Select directory with videos!");
        }
    }

    private void showVideoInfo(File selectedvideo) {
        videonameArea.setText(selectedvideo.getName());
        filepathArea.setText(selectedvideo.toString());
    }

    @Override
    public void start(Stage rootstage) throws IOException {

        VBox vBox = new VBox(menuBar);
        Scene scene = new Scene(vBox, 600, 300);
        rootstage.setResizable(false);
        rootstage.setScene(scene);
        rootstage.show();

        final String APPLICATION_WINDOW_TITLE = "Random Video Selector";
        rootstage.setTitle(APPLICATION_WINDOW_TITLE);

        menuBar.getMenus().add(fileMenu);
        menuBar.getMenus().add(editMenu);
        buildMenus(rootstage);

        videonameArea.setX(5);
        videonameArea.setY(300);
        filepathArea.setX(5);
        filepathArea.setY(350);

        vBox.getChildren().addAll(RefreshButton, videonameArea, filepathArea);

        RefreshButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if(MovieList.size() > 0){
                    selectRandomVideo(MovieList);
                }else{
                    videonameArea.setText("Select directory with videos!");
                }
            }
        });
    }

    public static void main(String[] args) {
        launch();
    }
}