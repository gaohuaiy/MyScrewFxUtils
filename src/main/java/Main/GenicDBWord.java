/*
 * Copyright (c) 2012, 2014 Oracle and/or its affiliates.
 * All rights reserved. Use is subject to license terms.
 *
 * This file is available and licensed under the following license:
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  - Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *  - Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the distribution.
 *  - Neither the name of Oracle nor the names of its
 *    contributors may be used to endorse or promote products derived
 *    from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package Main;

import cn.smallbun.screw.core.engine.EngineFileType;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import static javafx.geometry.HPos.RIGHT;

import javafx.event.EventType;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.sql.SQLOutput;

public class GenicDBWord extends Application {
    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void start(Stage primaryStage) {
        String[] DBtypeArrays = {"ORACLE", "MYSQL"};
        String[] FileTypeArrays = {"WORD", "MD", "HTML"};
        primaryStage.setTitle("数据库文档生成器");
        String IcoUrl = "数据库.png";
        primaryStage.getIcons().add(new Image(IcoUrl));

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);

        grid.setHgap(1);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Text scenetitle = new Text("Welcome");
        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(scenetitle, 0, 0, 2, 1);



        Label userName = new Label("用户名:");
        grid.add(userName, 0, 1);

        TextField userTextField = new TextField();
        grid.add(userTextField, 1, 1);

        Label pw = new Label("密码:");
        grid.add(pw, 0, 2);

        PasswordField pwBox = new PasswordField();
        grid.add(pwBox, 1, 2);

        Label fileType = new Label("文件类型:");
        grid.add(fileType, 0, 3);
        ChoiceBox FileType = new ChoiceBox(FXCollections.observableArrayList(FileTypeArrays));
        grid.add(FileType, 1, 3);

        Label dburl = new Label("dburl:");
        grid.add(dburl, 0, 4);

        TextField dbUrl = new TextField();
        dbUrl.setPrefWidth(300.0);
        grid.add(dbUrl, 1, 4);

        Label dbType = new Label("数据库类型:");
        grid.add(dbType, 0, 5);
        ChoiceBox DBtype = new ChoiceBox(FXCollections.observableArrayList(DBtypeArrays)
        );
        DBtype.setOnAction(event -> {
            String text = String.valueOf(DBtype.getSelectionModel().getSelectedItem());
            if (StringUtils.isEmpty(dbUrl.getText())) {
                if (text == DBtypeArrays[0]) {
                    dbUrl.setText("jdbc:oracle:thin:@localhost:1521:XE");
                }else if (text == DBtypeArrays[1]) {
                    dbUrl.setText("jdbc:mysql://localhost:3306");
                }
            }
        });
        grid.add(DBtype, 1, 5);

        Button btn = new Button("生成文档");
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(btn);
        grid.add(hbBtn, 1, 6);

        final Text actiontarget = new Text();
        grid.add(actiontarget, 0, 7);
        grid.setColumnSpan(actiontarget, 2);
        grid.setHalignment(actiontarget, RIGHT);
        actiontarget.setId("actiontarget");

        btn.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
                System.out.println(dbUrl.getText());
                System.out.println(userTextField.getText());
                System.out.println(pwBox.getText());
                System.out.println(FileType.getSelectionModel().getSelectedItem().toString());
                String GenicFileType = FileType.getSelectionModel().getSelectedItem().toString();
                EngineFileType engineFileType = null;
                switch (GenicFileType){
                    case "HTML": engineFileType = EngineFileType.HTML  ;break;
                    case "MD": engineFileType = EngineFileType.MD  ;break;
                    case "WORD": engineFileType = EngineFileType.WORD  ;break;
                    default :
                        try {
                            throw new Exception("请选择文件类型");
                        } catch (Exception exception) {
                            exception.printStackTrace();
                        }
                        break;
                }
                System.out.println(DBtype.getSelectionModel().getSelectedItem().toString());
                String dbtype = DBtype.getSelectionModel().getSelectedItem().toString();
                String DBTYPE = "";
                switch (dbtype){
                    case "MYSQL": DBTYPE = "com.mysql.cj.jdbc.Driver"  ;break;
                    case "ORACLE": DBTYPE = "oracle.jdbc.driver.OracleDriver" ;break;
                    default :
                        try {
                            throw new Exception("请选择数据库类型");
                        } catch (Exception exception) {
                            exception.printStackTrace();
                        }
                        break;
                }


                DirectoryChooser directoryChooser=new DirectoryChooser();
                File file = directoryChooser.showDialog(new Stage());
                String path = file.getPath();
                System.out.println(path);
                actiontarget.setFill(Color.FIREBRICK);
                actiontarget.setText("生成...");
                new WriteDoc().write(dbUrl.getText(),userTextField.getText(),pwBox.getText(),engineFileType,path,DBTYPE);
                actiontarget.setText("生成完毕！");

            }
        });

        Scene scene = new Scene(grid, 320, 285);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

}
