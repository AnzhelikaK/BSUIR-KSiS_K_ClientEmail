package com.bsuir.controller;

import com.bsuir.controller.dto.EMailDto;
import com.bsuir.controller.map.EMailDtoMapper;
import com.bsuir.repository.entity.EMail;
import com.bsuir.repository.entity.SendingMessage;
import com.bsuir.repository.entity.UserCredentials;
import com.bsuir.service.MailClientService;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.stream.Collectors;

@Component
@FxmlView("/main.fxml")
@RequiredArgsConstructor
@Slf4j
public class MainController {

    private final MailClientService mailClientService;
    private final EMailDtoMapper mapper;

    @FXML
    private TextField hostField;
    @FXML
    private TextField loginField;
    @FXML
    private PasswordField passwordField;

    @FXML
    private TableView<EMailDto> masterTable;

    @FXML
    private TableColumn<EMailDto, String> dateColumn;
    @FXML
    private TableColumn<EMailDto, String> fromColumn;
    @FXML
    private TableColumn<EMailDto, String> subjectColumn;

    @FXML
    private TextArea detailNode;

    private final ObjectProperty<EMailDto> currentEmail = new SimpleObjectProperty<>();

    private final ChangeListener<EMailDto> emailListener = (obs, oldEmail, newEmail) -> {
        if (newEmail == null) {
            detailNode.setText("");
        } else {
            detailNode.setText(String.format(
                "Date: %s%nSubject: %s%nFrom: %s%nText: %s",
                newEmail.getDate(),
                newEmail.getSubject(),
                newEmail.getFrom(),
                newEmail.getText()
            ));
        }
    };

    public void initialize() {
        currentEmailProperty().addListener(emailListener);
    }

    @FXML
    public void handleClickReceiveEmails(ActionEvent actionEvent) {
        // reset
        currentEmailProperty().unbind();

        // try to get credentials
        String host = hostField.getText();
        String login = loginField.getText();
        String password = passwordField.getText();
        UserCredentials userCredentials = UserCredentials.builder()
            .host(host)
            .login(login)
            .password(password)
            .build();

        // receive new emails
        Collection<EMail> eMails = mailClientService.receiveEmails(userCredentials);
        ObservableList<EMailDto> eMailDtos = FXCollections.observableList(eMails.stream().map(mapper::mapFrom).collect(Collectors.toList()));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        fromColumn.setCellValueFactory(new PropertyValueFactory<>("from"));
        subjectColumn.setCellValueFactory(new PropertyValueFactory<>("subject"));
        masterTable.setItems(eMailDtos);

        // binding
        currentEmailProperty().bind(masterTable.getSelectionModel().selectedItemProperty());

        log.info("Received emails: " + eMails);
    }

    public final ObjectProperty<EMailDto> currentEmailProperty() {
        return this.currentEmail;
    }

    ///////////// Send

    @FXML
    private TextField sendHostField;
    @FXML
    private TextField sendLoginField;
    @FXML
    private PasswordField sendPasswordField;
    @FXML
    private TextField toField;
    @FXML
    private TextField sendSubjectField;
    @FXML
    private TextArea sendText;

    public void handleClickSendEmail(ActionEvent actionEvent) {
        String host = sendHostField.getText();
        String login = sendLoginField.getText();
        String password = sendPasswordField.getText();

        String to = toField.getText();
        String subject = sendSubjectField.getText();
        String text = sendText.getText();

        UserCredentials credentials = UserCredentials.builder()
            .host(host)
            .login(login)
            .password(password)
            .build();
        SendingMessage message = SendingMessage.builder()
            .to(to)
            .subject(subject)
            .text(text)
            .date(LocalDateTime.now())
            .build();

        mailClientService.sendEmail(credentials, message);
        log.info("The message was sent");
    }
}
