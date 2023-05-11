package salesmanagement.salesmanagement.Utils;

import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.BooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import salesmanagement.salesmanagement.SalesComponent.Order;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * This is a utility class created to support programming projects.
 * It provides various helper methods that can be used in a project
 * to perform common tasks or operations.
 * <p>
 * This class should not be instantiated as all the methods are
 * static and can be accessed directly through the class name.
 * <p>
 * To use this class, simply import it into your project and call
 * the desired method using the class name followed by the method name.
 * <p>
 *
 * @author THANH AN
 * @since 1.0
 */

public class Utils {
    /**
     * Returns a list of all descendant nodes of the specified root node, including the root node itself.
     *
     * @param root the root node of the tree to traverse
     * @return an ArrayList of all nodes in the tree
     * @throws NullPointerException if the root node is null
     * @implNote This method uses a recursive approach to traverse the tree structure, starting from the
     * root node and iterating over its children to collect all descendant nodes. The implementation uses
     * the {@code getChildrenUnmodifiable} method to retrieve the list of children for each parent node.
     * The returned list includes the root node as the first element, followed by its descendant nodes in
     * a depth-first order.
     */
    public static ArrayList<Node> getAllNodes(Parent root) {
        ArrayList<Node> nodes = new ArrayList<>();
        addAllDescendents(root, nodes);
        return nodes;
    }

    /**
     * Adds all the descendant nodes of the specified parent node to the specified ArrayList.
     * This method does not clear the nodes list before adding nodes.
     *
     * @param parent the parent node to traverse
     * @param nodes  the ArrayList to add nodes to
     * @implNote This method is used by the {@code getAllNodes} method to recursively traverse the
     * tree structure and collect all descendant nodes. The implementation uses the {@code getChildrenUnmodifiable}
     * method to retrieve the list of children for each parent node. If a child node is itself a parent node,
     * this method is called recursively to add its descendants to the nodes list.
     */
    private static void addAllDescendents(Parent parent, ArrayList<Node> nodes) {
        for (Node node : parent.getChildrenUnmodifiable()) {
            nodes.add(node);
            if (node instanceof Parent)
                addAllDescendents((Parent) node, nodes);
        }
    }

    /**
     * Locks all child nodes of the given parent node that are instances of the provided locked classes,
     * except for any nodes specified in the list of exception nodes. Locking a node disables its interactivity
     * and may also modify other properties or attributes of the node.
     *
     * @param parent         the parent node whose child nodes should be locked
     * @param lockedClasses  a list of classes to identify the types of nodes to lock
     * @param exceptionNodes a list of nodes to exclude from the locking process
     * @throws NullPointerException if the parent node or any input list is null
     * @implNote This method uses the {@code isInstance} method to check if a node is an instance of any of the
     * locked classes. To unlock a node, it must be included in the list of exception nodes. This method iterates
     * over all the child nodes of the given parent node, so the performance may be affected for large node trees.
     */
    public void lockAllChildren(Parent parent, List<Class<?>> lockedClasses, List<Node> exceptionNodes) {
        if (parent == null || lockedClasses == null || exceptionNodes == null) {
            return;
        }

        if (lockedClasses.isEmpty()) {
            return;
        }

        for (Node node : getAllNodes(parent)) {
            for (Class<?> c : lockedClasses) {
                if (c.isInstance(node)) {
                    node.setDisable(true);
                }
            }
            if (exceptionNodes.contains(node)) {
                node.setDisable(false);
            }
        }
    }


    /**
     * Returns the name and extension of a file from a given address.
     *
     * @param address the address of the file, e.g. "D:\\DOWNLOAD\\commons-net-3.9.0-src.zip".
     * @return a string containing the file name and extension, e.g. "commons-net-3.9.0-src.zip".
     */
    public static String getFileNameAndExtension(String address) {
        File file = new File(address);
        String fileName = file.getName();
        String extension = "";
        int index = fileName.lastIndexOf(".");
        if (index > 0) {
            extension = fileName.substring(index + 1);
        }
        return fileName + "." + extension;
    }

    /**
     * This method shakes a given JavaFX Node horizontally for a short duration.
     * It creates a Timeline object that animates the translateX property of the given node
     * with a set of KeyFrames that define the movement of the node.
     * After the animation is finished, it sets the translateX property back to 0.
     *
     * @param node the JavaFX Node to shake
     */
    public static void shake(Node node) {
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(node.translateXProperty(), 0)),
                new KeyFrame(Duration.millis(100), new KeyValue(node.translateXProperty(), -5)),
                new KeyFrame(Duration.millis(200), new KeyValue(node.translateXProperty(), 5)),
                new KeyFrame(Duration.millis(300), new KeyValue(node.translateXProperty(), -5)),
                new KeyFrame(Duration.millis(400), new KeyValue(node.translateXProperty(), 5)),
                new KeyFrame(Duration.millis(500), new KeyValue(node.translateXProperty(), 0))
        );
        timeline.setOnFinished(event -> {
            node.setTranslateX(0);
        });
        timeline.play();
    }

    /**
     * Adds a skeleton effect to an ImageView by fading in and out a grey background with rounded corners.
     * <p>
     * The effect is automatically paused and resumed based on the value of a BooleanProperty that represents
     * whether the image is still loading or not.
     *
     * @param imageView the ImageView to add the skeleton effect to
     * @return a BooleanProperty that represents whether the image is still loading or not
     */
    public static BooleanProperty skeletonEffect(ImageView imageView) {
        BooleanProperty avatarLoading = new javafx.beans.property.SimpleBooleanProperty(true);
        StackPane avatarLayer = (StackPane) ((StackPane) imageView.getParent()).getChildren().get(1);
        avatarLayer.setStyle("-fx-background-color: grey;-fx-background-radius: 10;");

        FadeTransition imageFadeTransition = new FadeTransition(Duration.seconds(1.2), avatarLayer);
        imageFadeTransition.setFromValue(0.2);
        imageFadeTransition.setToValue(0.4);
        imageFadeTransition.setCycleCount(Timeline.INDEFINITE);
        imageFadeTransition.setAutoReverse(true);
        imageFadeTransition.play();

        ChangeListener<Boolean> imageLoadingListener = (observable, oldValue, newValue) -> {
            if (newValue) {
                avatarLayer.setStyle("-fx-background-color: grey;-fx-background-radius: 10;");
                imageFadeTransition.play();
            } else {
                imageFadeTransition.pause();
                avatarLayer.setStyle("-fx-background-color: transparent");
            }
        };
        avatarLoading.addListener(imageLoadingListener);

        return avatarLoading;
    }

    /**
     * Exports the data from a ResultSet to an Excel file, given a list of selected columns to include.
     * <p>
     * Creates a new sheet named "Result" in the workbook and writes the selected columns as headers
     * <p>
     * in the first row of the sheet, and the data from the ResultSet in subsequent rows.
     *
     * @param resultSet       the ResultSet containing the data to be exported
     * @param outputFile      the output file where the data will be exported to
     * @param selectedColumns the list of column names to be included in the exported data
     * @throws Exception if an error occurs while creating the Excel file or writing the data to it
     */
    public static void exportToExcel(ResultSet resultSet, File outputFile, List<String> selectedColumns) throws Exception {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Result");
        ResultSetMetaData metaData = resultSet.getMetaData();
        int columnCount = metaData.getColumnCount();

        Row headerRow = sheet.createRow(0);
        int columnIndex = 0;
        for (int i = 1; i <= columnCount; i++) {
            String columnName = metaData.getColumnName(i);
            System.out.println(columnName);
            if (selectedColumns.contains(columnName)) {
                Cell cell = headerRow.createCell(columnIndex++);
                cell.setCellValue(columnName);
            }
        }

        int rowIndex = 1;
        while (resultSet.next()) {
            Row dataRow = sheet.createRow(rowIndex++);
            columnIndex = 0;
            for (int i = 1; i <= columnCount; i++) {
                String columnName = metaData.getColumnName(i);
                if (selectedColumns.contains(columnName)) {
                    Object value = resultSet.getObject(i);
                    Cell cell = dataRow.createCell(columnIndex++);
                    if (value != null) {
                        if (value instanceof String) {
                            cell.setCellValue((String) value);
                        } else if (value instanceof Integer) {
                            cell.setCellValue((Integer) value);
                        } else if (value instanceof Double) {
                            cell.setCellValue((Double) value);
                        } else if (value instanceof Date dateValue) {
                            CellStyle dateStyle = workbook.createCellStyle();
                            dateStyle.setDataFormat(workbook.getCreationHelper().createDataFormat().getFormat("dd/MM/yyyy"));
                            cell.setCellValue(dateValue);
                            cell.setCellStyle(dateStyle);
                        } else {
                            cell.setCellValue(value.toString());
                        }
                    }
                }
            }
        }
        FileOutputStream outputStream = new FileOutputStream(outputFile);
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();
    }

    /**
     * This method adjusts the size of the columns in a TableView based on the specified ratios.
     * The method listens to the widthProperty of the TableView, and adjusts the column widths
     * every time the width of the table changes. The column ratios specify the relative size of
     * each column compared to the total width of the table.
     *
     * @param <T>          the type of the items in the TableView
     * @param table        the TableView whose column sizes will be adjusted
     * @param columnWidthRatios a list of ratios that specify the relative size of each column. This list
     *                     must have the same size as the number of columns in the table.
     *                     If the list is not the same size as the number of columns in the table,
     *                     the behavior of this method is undefined.
     **/
    public static <T> void adjustTableColumnWidths(TableView<T> table, List<Double> columnWidthRatios) {
        table.widthProperty().addListener((obs, oldWidth, newWidth) -> {
            double tableWidth = newWidth.doubleValue();
            for (int i = 0; i < columnWidthRatios.size(); i++) {
                TableColumn<T, ?> col = table.getColumns().get(i);
                col.setMaxWidth(tableWidth * columnWidthRatios.get(i));
                col.setPrefWidth(tableWidth * columnWidthRatios.get(i));
            }
        });
    }
}

