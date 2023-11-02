package com.spring.CMS.ServicesImpl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import org.apache.pdfbox.io.IOUtils;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.spring.CMS.DAO.BillDAO;
import com.spring.CMS.JWT.JwtFilter;
import com.spring.CMS.POJO.Bill;
import com.spring.CMS.Services.BillService;
import com.spring.CMS.Utils.CafeUtility;
import com.spring.CMS.constants.CafeConstants;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class BillServiceImpl implements BillService {

	@Autowired
	JwtFilter jwtFilter;

	@Autowired
	BillDAO billDao;

	@Override
	public ResponseEntity<String> generateReport(Map<String, Object> requestMap) {
		log.info("Inside generateReport");
		try {
			String fileName;
			if (validateRequestMap(requestMap)) {
				if (requestMap.containsKey("isGenerate") && !(Boolean) requestMap.containsKey("isGenerate")) {
					fileName = (String) requestMap.get("uuid");
				} else {
					fileName = CafeUtility.getUUID();
					requestMap.put("uuid", fileName);
					insertBill(requestMap);
				}
				String pdfData = "Name: " + requestMap.get("name") + "\n" + "Contact Number: "
						+ requestMap.get("contactNumber") + "\n" + "Email: " + requestMap.get("email") + "\n"
						+ "Payment Method: " + requestMap.get("paymentMethod");
				Document document = new Document();
				PdfWriter.getInstance(document,
						new FileOutputStream(CafeConstants.STORE_LOCATION + "\\" + fileName + ".pdf"));
				document.open();
				setRectangleInPdf(document);
				Paragraph para = new Paragraph("Cafe Management System", getFont("Header"));
				para.setAlignment(Element.ALIGN_CENTER);
				document.add(para);

				Paragraph para2 = new Paragraph(pdfData + "\n \n", getFont("Data"));
				document.add(para2);

				PdfPTable table = new PdfPTable(5);
				table.setWidthPercentage(100);
				addTableHeader(table);

				JSONArray jsonArray = CafeUtility.getJsonArray((String) requestMap.get("productDetails"));
				for (int i = 0; i < jsonArray.length(); i++) {
					addRows(table, CafeUtility.getMapFromJson(jsonArray.getString(i)));
				}
				document.add(table);

				Paragraph footer = new Paragraph("Total : " + requestMap.get("totalAmount") + "\n"
						+ "Thank You For Visting!" + "\n" + "Please Visit Again.", getFont("Data"));
				document.add(footer);
				document.close();
				return new ResponseEntity<>("{\"uuid\":\"" + fileName + "\"}", HttpStatus.OK);
			}
			return CafeUtility.getResponseEntity("Required Data not found.", HttpStatus.BAD_REQUEST);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return CafeUtility.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	private void insertBill(Map<String, Object> requestMap) {
		try {
			Bill bill = new Bill();
			bill.setUuid((String) requestMap.get("uuid"));
			bill.setName((String) requestMap.get("name"));
			bill.setEmail((String) requestMap.get("email"));
			bill.setContactNumber((String) requestMap.get("contactNumber"));
			bill.setPaymentMethod((String) requestMap.get("paymentMethod"));
			bill.setTotal(Integer.parseInt((String) requestMap.get("totalAmount")));
			bill.setProductDetail((String) requestMap.get("productDetails"));
			bill.setCreatedBy(jwtFilter.getCurrentUser());
			billDao.save(bill);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	private boolean validateRequestMap(Map<String, Object> requestMap) {
		return requestMap.containsKey("name") && requestMap.containsKey("contactNumber")
				&& requestMap.containsKey("email") && requestMap.containsKey("paymentMethod")
				&& requestMap.containsKey("productDetails") && requestMap.containsKey("totalAmount");
	}

	private void setRectangleInPdf(Document document) throws DocumentException {
		log.info("Inside setRectangleInPdf");
		Rectangle rect = new Rectangle(577, 855, 18, 15);
		rect.enableBorderSide(1);
		rect.enableBorderSide(2);
		rect.enableBorderSide(4);
		rect.enableBorderSide(8);
		rect.setBorderColor(BaseColor.BLACK);
		rect.setBorderWidth(1);
		document.add(rect);
	}

	private Font getFont(String type) {
		log.info("Inside getFont");
		switch (type) {
		case "Header":
			Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLDOBLIQUE, 18, BaseColor.BLACK);
			headerFont.setStyle(Font.BOLD);
			return headerFont;
		case "Data":
			Font dataFont = FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, BaseColor.BLACK);
			dataFont.setStyle(Font.BOLD);
			return dataFont;

		default:
			return new Font();
		}
	}

	private void addTableHeader(PdfPTable table) {
		log.info("Inside addTableHeader");
		Stream.of("Name", "Category", "Quantity", "Price", "Sub Total").forEach(columnTitle -> {
			PdfPCell header = new PdfPCell();
			header.setBackgroundColor(BaseColor.LIGHT_GRAY);
			header.setBorderWidth(2);
			header.setPhrase(new Phrase(columnTitle));
			header.setBackgroundColor(BaseColor.CYAN);
			header.setHorizontalAlignment(Element.ALIGN_CENTER);
			header.setVerticalAlignment(Element.ALIGN_CENTER);
			table.addCell(header);
		});

	}

	private void addRows(PdfPTable table, Map<String, Object> data) {
		log.info("Inside addRows");
		table.addCell((String) data.get("name"));
		table.addCell((String) data.get("category"));
		table.addCell((String) data.get("quantity"));
		table.addCell(Double.toString((Double) data.get("price")));
		table.addCell(Double.toString((Double) data.get("total")));
	}

	@Override
	public ResponseEntity<List<Bill>> getBills() {
		List<Bill> list = new ArrayList<>();
		if (jwtFilter.isAdmin()) {
			list = billDao.getAllBills();
		} else {
			list = billDao.getBillByUsername(jwtFilter.getCurrentUser());
		}
		return new ResponseEntity<>(list, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<byte[]> getPdf(Map<String, Object> requestMap) {
		log.info("Inside getPdf : requestMap {}", requestMap);
		try {
			byte[] byteArray = new byte[0];
			if (!requestMap.containsKey("uuid") && validateRequestMap(requestMap)) {
				return new ResponseEntity<>(byteArray, HttpStatus.BAD_REQUEST);
			}
			String filePath = CafeConstants.STORE_LOCATION + "\\" + (String) requestMap.get("uuid") + ".pdf";
			if (CafeUtility.isFileExist(filePath)) {
				byteArray = getByteArray(filePath);
				return new ResponseEntity<>(byteArray, HttpStatus.OK);
			} else {
				requestMap.put("isGenerate", false);
				generateReport(requestMap);
				byteArray = getByteArray(filePath);
				return new ResponseEntity<>(byteArray, HttpStatus.OK);
			}
		} catch (Exception ex) {

		}
		return null;
	}

	private byte[] getByteArray(String filePath) throws Exception {
		File initialFile = new File(filePath);
		FileInputStream targetStream = new FileInputStream(initialFile);
		byte[] byteArray = IOUtils.toByteArray(targetStream);
		targetStream.close();
		return byteArray;
	}

	@Override
	public ResponseEntity<String> deleteBill(Integer id) {
		try {
			Optional optional = billDao.findById(id);
			if(!optional.isEmpty()) {
				billDao.deleteById(id);
				return CafeUtility.getResponseEntity("Bill Deleted Successfully.",HttpStatus.OK);
			}
			return CafeUtility.getResponseEntity("Bill id does not exist",HttpStatus.OK);
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		return CafeUtility.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
