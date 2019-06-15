package core_app.bwg;

//import com.sun.org.apache.xpath.internal.operations.String;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.MapValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import core_app.visiolization.graph.VisEdge;
import core_app.visiolization.graph.VisGraph;
import core_app.visiolization.graph.VisNode;

import java.util.*;

/**
 * App Controller
 * @author ssvs
 */
public class Controller {
    private String[][] matrix;

    @FXML
    private TableView tableNodes;

    private int nextChar = 0;
    private CustomLogger applog;

    private VisGraph visGraph = null;

    @FXML
    TextArea regionTextAreaLog;

    /**
     * Get data on table
     */
    public void getData() {
        tableNodes.refresh();
        /*for (int i = 0; i < nextChar-1; i++) {
            for (int j = 1; j < nextChar; j++) {
                System.out.print(getValueAt(tableNodes, j, i));
            }
            System.out.println();
        }*/
        matrix = getDataTableToString();
        System.out.println("tree:");
        StringBuilder outPrint = new StringBuilder();
        for (String[] ints : matrix) {
            for (int j = 0; j < matrix[0].length; j++) {
                System.out.print(ints[j] + " ");
                outPrint.append(ints[j]).append(" ");
            }
            System.out.println();
            outPrint.append("\n");
        }
        applog.appendTextLogToTextArea("Graph init");
        applog.appendTextLogToTextArea(outPrint.toString());
    }
    private static Object getValueAt(TableView table, int column, int row) {
        return  ((TableColumnBase) table.getColumns().get(column)).getCellObservableValue(row).getValue();
    }
    private String[][] getDataTableToString() {
        String[][] m = new String[arr2.length][arr1.length];
        for (int i = 0; i < arr2.length; i++) {
            for (int j = 1; j < arr1.length+1; j++) {
                m[i][j-1] = getValueAt(tableNodes, j, i).toString();
            }
        }
        return m;
    }


    @FXML
    private TextField aNodes_input;
    @FXML
    private TextField aABC_input;
    @FXML
    private TextField aStart_input;
    @FXML
    private TextField aStop_input;

    @FXML
    private TextField aTestLine_input;

    private String[] arr1 = new String[]{"1","2","3"};
    private String[] arr2 = new String[]{"q1","q2","q3","q4","q5"};

    /**
     * Get Options on Table
     */
    public void setOptions() {
        arr1 = aABC_input.getText().split(" ");
        ArrayList<String> arr11 = new ArrayList<>(Arrays.asList(arr1));
        arr11.add(0, "-");
        arr2 = aNodes_input.getText().split(" ");


        ObservableList allData = tableNodes.getItems();
        allData.clear();
        tableNodes.getItems().clear();
        tableNodes.getColumns().clear();
        tableNodes.refresh();
        nextChar = 0;

        for (int i = 0; i <= arr1.length; i++) {
            addNode(arr11.get(i));
        }
        for (String s : arr2) {
            addTextRow(s);
        }
    }


    String[] testLine = new String[]{};

    /**
     * Action Calc
     */
    public void calc(){
        testLine = aTestLine_input.getText().split(" ");
        getData();
        //visGraph = getDummyGraph();
    }

    /**
     * Get Graph ViewPort
     */
    public void getGraph() {
        getData();
        visGraph = getDummyGraph();
        if (visGraph == null) {
            applog.appendTextLogToTextArea("Graph is not set!");
            return;
        }
        Region regionWebView = new Browser(visGraph);
        Stage stage = new Stage();
        stage.setTitle("Network Graph");
        Scene scene = new Scene(regionWebView,  Color.web("#ffffff"));
        stage.setScene(scene);
        stage.show();
    }


    private VisGraph getDummyGraph() {
        //getData();
        VisGraph graph = new VisGraph();

        for (int j = 0; j < arr2.length; j++) {
            graph.addNodes(new VisNode(j, arr2[j]));
        }

        String[][] matrixLabels = new String[arr2.length][arr2.length];
        for (int i = 0; i < arr2.length; i++)
            for (int j = 0; j < arr2.length; j++)
                matrixLabels[i][j] = "";

        for (int i = 0; i < arr2.length; i++) {
            for (int j = 0; j < arr1.length; j++) {
                String[] path = matrix[i][j].split(" ");
                for (String s : path)
                    for (int ss = 0; ss < arr2.length; ss++)
                        if (s.equals(arr2[ss]))
                            matrixLabels[i][ss] = (matrixLabels[i][ss].equals("")) ? arr1[j] : matrixLabels[i][ss] + ";" + arr1[j];
            }
        }

        for (int i = 0; i < arr2.length; i++)
            for (int j = 0; j < arr1.length; j++)
                if (!matrix[i][j].equals("")) {
                    String[] path = matrix[i][j].split(" ");
                    for (String s : path)
                        for (int ss = 0; ss < arr2.length; ss++)
                            if (s.equals(arr2[ss])) {
                                VisEdge edges = new VisEdge(
                                        new VisNode(ss, ""),
                                        new VisNode(i, ""),
                                        "to;from", String.valueOf(matrixLabels[i][ss])
                                );
                                graph.addEdges(edges);
                            }


                }

        return graph;
    }
    @FXML
    public void initialize() {
        applog = new CustomLogger(this.getClass().getName());
        //applog.logger.info("first test");
        applog.setTextArea(regionTextAreaLog);
        applog.appendTextLogToTextArea("App init");
    }
    /**
     * Delete
     */
    public void deleteNode() {
        if (nextChar == 0) return;
        tableNodes.getColumns().remove(
                tableNodes.getColumns().size()-1);
        tableNodes.getItems().remove(tableNodes.getItems().size()-1);

        nextChar--;
    }

    /**
     * Add Matrix Cell in Table
     */
    public void addNode(String text) {
        String mapChar = String.valueOf(nextChar++);
        TableColumn column = new TableColumn<>("Class " + mapChar);
        column.setCellValueFactory(new MapValueFactory(mapChar));
        column.setEditable(true);
        column.setCellFactory(TextFieldTableCell.forTableColumn());
        column.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<HashMap, String>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<HashMap, String> event) {
                        ObservableList allData = tableNodes.getItems();
                        ObservableList<Map> rezervData = FXCollections.observableArrayList(allData);
                        allData.clear();
                        for (int i = 0; i <= arr1.length+1; i++) {
                            Map<String, String> dataRow = new HashMap<>();
                            for (int j = 0; j <= arr2.length; j++) {
                                //System.out.print(getValueAt(tableNodes, j, i));
                                if (i == event.getTablePosition().getRow() && j == event.getTablePosition().getColumn()) {
                                    String mapKey = String.valueOf(event.getTablePosition().getColumn());
                                    String value1 = event.getNewValue();
                                    dataRow.put(mapKey, value1);
                                } else {
                                    Map map = rezervData.get(i);
                                    String mapKey = String.valueOf(j);
                                    Iterator it = map.entrySet().iterator();
                                    String value1 = "{0}";
                                    while (it.hasNext()) {
                                        Map.Entry pair = (Map.Entry)it.next();
                                        if (String.valueOf(j).equals(pair.getKey().toString())) {
                                            value1 = pair.getValue().toString();
                                            break;
                                        }
                                        it.remove();
                                    }
                                    dataRow.put(mapKey, value1);
                                }
                            }
                            allData.add(dataRow);
                        }
                        tableNodes.refresh();
                    }
                }
        );
        //column.setText(String.valueOf(nextChar - 2));
        column.setText(text);
        column.setSortable(false);
        column.setMaxWidth(40);
        tableNodes.getColumns().add(column);
        tableNodes.setEditable(true);
    }
    /**
     * Add Matrix Row in table
     */
    private void addTextRow(String text) {
        ObservableList<Map> allData = tableNodes.getItems();
        //int offset = allData.size();
        Map<String, String> dataRow = new HashMap<>();
        for (int j = 0; j < arr2.length; j++) {
            String mapKey = String.valueOf(j);
            String value1 = "{0}";
            dataRow.put(mapKey, value1);

            mapKey = String.valueOf(nextChar-1 );
            value1 = "{0}";
            dataRow.put(mapKey, value1);

            mapKey = String.valueOf(0);
            value1 = String.valueOf(text);
            dataRow.put(mapKey, value1);
        }
        allData.add(dataRow);
    }
}
