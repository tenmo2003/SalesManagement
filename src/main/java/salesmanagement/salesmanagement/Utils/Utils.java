package salesmanagement.salesmanagement.Utils;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.util.Duration;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A utility class providing common functionality for working with JavaFX nodes and node trees.
 *
 * @author THANHAN
 * @since 1.4
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
}

