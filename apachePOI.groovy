@Grapes([
    @Grab(group='org.apache.poi', module='poi', version='3.9'),
    @Grab(group='org.apache.poi', module='poi-ooxml', version='3.9'),
    @Grab(group='org.apache.poi', module='poi-ooxml-schemas', version='3.7'),
    @Grab(group='org.apache.xmlbeans', module='xmlbeans', version='2.3.0'),
    @Grab(group='org.apache.geronimo.specs', module='geronimo-stax-api_1.0_spec', version='1.0.1'),
    @Grab(group='dom4j', module='dom4j', version='1.6.1')
])

import org.apache.poi.ss.usermodel.*
import org.apache.poi.ss.util.*

InputStream inp = new FileInputStream("xxx_666.xlsx");
 
Workbook wb = WorkbookFactory.create(inp);
Sheet sheet = wb.getSheet("PKD");

//FIrst row with pkd
CellReference cellRefBegin = new CellReference("V4");
//LAst row with PKD
CellReference cellRefEnd = new CellReference("V618");

//FIREFLEXA
CellReference cellRefFireflexa = new CellReference("N4");
//TPL
CellReference cellRefTpl = new CellReference("O4");
//THEFT
CellReference cellRefTheft = new CellReference("P4");

for (int i = cellRefBegin.getRow();  i<= cellRefEnd.getRow() ;i++) {
    Row row = sheet.getRow(i);
    Cell cellPkd = row.getCell(cellRefBegin.getCol());
    Cell cellFireflexa = row.getCell(cellRefFireflexa.getCol());
    Cell cellTpl       = row.getCell(cellRefTpl.getCol());
    Cell cellTheft     = row.getCell(cellRefTheft.getCol());
    println cellPkd.getStringCellValue() + '=' + cellFireflexa.getStringCellValue() + ',' + cellTpl.getStringCellValue() + ',' + cellTheft.getStringCellValue()
}

if (cell == null)
    cell = row.createCell(3);
cell.setCellType(Cell.CELL_TYPE_STRING);
cell.setCellValue("a test");
 
 Write the output to a file
FileOutputStream fileOut = new FileOutputStream("workbook.xls");
wb.write(fileOut);
fileOut.close();