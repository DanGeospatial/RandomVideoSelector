module com.dan.randomvideoselector {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.ikonli.javafx;

    opens com.dan.randomvideoselector to javafx.fxml;
    exports com.dan.randomvideoselector;
}