package core_app.bwg;

import javafx.scene.control.TextArea;

import java.util.logging.Logger;

public class CustomLogger  {
    private Logger logger = null;
    private static TextArea textArea = null;

    public CustomLogger(String classname) {
        logger = Logger.getLogger(classname);
    }

    public void warning(String text) {
        this.logger.warning(text);
    }

    public void info(String text) {
        this.logger.info(text);
    }

    void textUpdated() {
        textArea.setScrollTop(0);
        textArea.setScrollLeft(0);
    }

    public void setTextLogToTextArea(String text){
        logger.info(text);
        if (textArea == null) {
            this.logger.warning("TextArea = null");
            return;
        }
        textArea.setText(textArea.getText() + "\n" + text);
        textUpdated();
    }

    public void appendTextLogToTextArea(String text){
        logger.info(text);
        if (textArea == null) {
            this.logger.warning("TextArea = null");
            return;
        }
        textArea.appendText(  "\n" + text);
        textUpdated();
    }

    public void clearTextArea() {
        logger.info("clear TextArea");
        if (this.textArea == null) {
            this.logger.warning("TextArea = null");
            return;
        }
        this.textArea.clear();
    }

    public void setTextArea(TextArea textArea) {
        this.logger.fine(textArea.toString());
        this.textArea = textArea;
    }
}
