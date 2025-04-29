package question4;

public class ImprovedCloneSheet {
	/**
	 * Clones a sheet from the workbook, creating a deep copy with a new name.
	 *
	 * @param sheetNum the index of the sheet to clone
	 * @param newName the name for the cloned sheet
	 * @return the cloned XSSFSheet
	 * @throws IllegalArgumentException if the sheet index or name is invalid
	 * @throws POIXMLException if an error occurs during cloning
	 */
	public XSSFSheet cloneSheet(int sheetNum, String newName) {
	    validateSheetIndex(sheetNum);
	    XSSFSheet srcSheet = sheets.get(sheetNum);

	    String cloneName = (newName == null) ? getUniqueSheetName(srcSheet.getSheetName()) : newName;
	    validateSheetName(cloneName);

	    XSSFSheet clonedSheet = createSheet(cloneName);

	    copyRelations(srcSheet, clonedSheet);
	    copyExternalRelationships(srcSheet, clonedSheet);
	    cloneSheetContent(srcSheet, clonedSheet);
	    fixUnsupportedFeatures(clonedSheet);
	    clonedSheet.setSelected(false);
	    cloneDrawings(srcSheet, clonedSheet);

	    return clonedSheet;
	}

	/**
	 * Copies internal relations from the source to the cloned sheet.
	 */
	private void copyRelations(XSSFSheet srcSheet, XSSFSheet clonedSheet) {
	    for (RelationPart rp : srcSheet.getRelationParts()) {
	        POIXMLDocumentPart part = rp.getDocumentPart();
	        if (!(part instanceof XSSFDrawing)) {
	            addRelation(rp, clonedSheet);
	        }
	    }
	}

	/**
	 * Copies external relationships from the source to the cloned sheet.
	 */
	private void copyExternalRelationships(XSSFSheet srcSheet, XSSFSheet clonedSheet) {
	    try {
	        for (PackageRelationship pr : srcSheet.getPackagePart().getRelationships()) {
	            if (pr.getTargetMode() == TargetMode.EXTERNAL) {
	                clonedSheet.getPackagePart().addExternalRelationship(
	                        pr.getTargetURI().toASCIIString(),
	                        pr.getRelationshipType(),
	                        pr.getId());
	            }
	        }
	    } catch (InvalidFormatException e) {
	        throw new POIXMLException("Failed to clone external relationships", e);
	    }
	}

	/**
	 * Clones the content of the sheet.
	 */
	private void cloneSheetContent(XSSFSheet srcSheet, XSSFSheet clonedSheet) {
	    try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
	        srcSheet.write(out);
	        try (ByteArrayInputStream bis = new ByteArrayInputStream(out.toByteArray())) {
	            clonedSheet.read(bis);
	        }
	    } catch (IOException e) {
	        throw new POIXMLException("Failed to clone sheet content", e);
	    }
	}

	/**
	 * Fixes unsupported features like legacy drawings and page setup.
	 */
	private void fixUnsupportedFeatures(XSSFSheet clonedSheet) {
	    CTWorksheet ct = clonedSheet.getCTWorksheet();

	    if (ct.isSetLegacyDrawing()) {
	        logger.log(POILogger.WARN, "Cloning sheets with comments is not yet fully supported.");
	        ct.unsetLegacyDrawing();
	    }
	    if (ct.isSetPageSetup()) {
	        logger.log(POILogger.WARN, "Cloning sheets with page setup is not yet fully supported.");
	        ct.unsetPageSetup();
	    }
	}

	/**
	 * Clones drawings and their relationships if present.
	 */
	private void cloneDrawings(XSSFSheet srcSheet, XSSFSheet clonedSheet) {
	    XSSFDrawing srcDrawing = getSheetDrawing(srcSheet);
	    if (srcDrawing == null) {
	        return;
	    }

	    CTWorksheet ct = clonedSheet.getCTWorksheet();
	    if (ct.isSetDrawing()) {
	        ct.unsetDrawing();
	    }

	    XSSFDrawing clonedDrawing = clonedSheet.createDrawingPatriarch();
	    clonedDrawing.getCTDrawing().set(srcDrawing.getCTDrawing());

	    List<RelationPart> srcRels = srcSheet.createDrawingPatriarch().getRelationParts();
	    for (RelationPart rp : srcRels) {
	        addRelation(rp, clonedDrawing);
	    }
	}

	/**
	 * Retrieves the drawing part from a sheet if it exists.
	 */
	private XSSFDrawing getSheetDrawing(XSSFSheet sheet) {
	    for (RelationPart rp : sheet.getRelationParts()) {
	        if (rp.getDocumentPart() instanceof XSSFDrawing) {
	            return (XSSFDrawing) rp.getDocumentPart();
	        }
	    }
	    return null;
	}

}
