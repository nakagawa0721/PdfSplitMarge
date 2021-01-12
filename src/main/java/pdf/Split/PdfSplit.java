//package pdf.Split;
//
//import java.io.File;
//import java.io.IOException;
//import java.util.ArrayList;
//
//import org.apache.pdfbox.pdmodel.PDDocument;
//import org.apache.pdfbox.pdmodel.PDPage;
//import org.apache.pdfbox.text.PDFTextStripper;
//
//public class PdfSplit {
//
//	public String[] pdfPage ;
//
//	//明細書の桁数
//	private int DETAILS_NUMBER = 7;
//
//	//給与賞与検索
//	private String SALARY_BONUS_MATCHES = "明細書";
//	//給与
//	private String SALARY_MATCHE = "給与";
//	//賞与
//	private String BONUS_MATCHE = "賞与";
//
//	SalarySplit ssp = new SalarySplit();
//
//	BonusSplit bsp = new BonusSplit();
//
//	TransferDirCreate tdc = new TransferDirCreate();
//
//	ArrayList<String> pdfList = new ArrayList<String>();
//
//
//	public void SplitCreate(String pdf) {
//
//
//		StringBuilder strb = new StringBuilder(pdf);
//
//		String p = null;
//
//		String targetPath = null;
//
//		String details;
//
//		String fileName = null;
//
//		String pdfname = null;
//
//		for(int i = strb.length() -1 ; i > 0 ; i--) {
//			p = String.valueOf(strb.charAt(i));
//			//ファイル名を削除
//			if(p.equals("\\")) {
//				strb.setLength(strb.length() - (strb.length() - i));
//				targetPath = strb.toString();
//				break;
//			}
//		}
//
//		File pdfFile = new File(pdf);
//
//		try {
//
//			if(pdfFile.isFile()) {
//				PDDocument pdDoc = PDDocument.load(pdfFile);
//				int numberOfPages = pdDoc.getNumberOfPages();
//
//				for(int n = 0; n < numberOfPages; n++) {
//
//					// ドキュメントオブジェクトの作成
//					PDDocument pdc = new PDDocument();
//					//ページオブジェクトの作成
//					PDPage page = (PDPage) pdDoc.getPage( n );
//					pdc.addPage(page);
//
//					//PDFf内のテキストを取得
//					PDFTextStripper stripper = new PDFTextStripper();
//					String text = stripper.getText(pdc);
//					String strtext = text;
//
//					//改行ごとに配列に追加
//					pdfPage = strtext.split("\n");
//
//					//明細名を取得する
//					details = getDetails();
//
//					if(details.equals("給与明細")) {
//						//給与明細分割処理
//						fileName = ssp.salarySplitPdf(pdfPage);
//					}else if(details.equals("賞与明細")) {
//						//賞与明細分割処理
//						fileName = bsp.bonusSplitPdf(pdfPage);
//					}
//
//
//					//保存先フォルダが存在しない場合、新規作成
//					String savePath = targetPath + "\\" + details;
//					File newdir = new File(savePath);
//					if(!newdir.exists()) {
//						newdir.mkdir();
//					}
//
//					// ファイル名を指定
//					pdfname = savePath + "\\" + "【" + details + "】" + fileName;
//
//					// 保存
//					pdc.save(pdfname);
//					pdc.close();
//				}
//
//			}else {
//				//振込明細
//				//tdc.TransferDirCreate(pdf,targetPath);
//				pdfname = pdf;
//
//			}
//
//			pdfList.add(pdfname);
//		}catch (IOException e) {
//			// TODO: handle exception
//			e.printStackTrace();
//		}
//
//		System.out.println("Create PDF File Complete!!");
//
//	}
//
//	/**
//	 * 給与明細か賞与明細かを判別する
//	 *
//	 * @return str
//	 */
//	private  String getDetails() {
//
//		String str = null;
//
//		for(int i = 0; i < pdfPage.length; i++) {
//			if(pdfPage[i].trim().length() >= DETAILS_NUMBER) {
//				str = pdfPage[i].trim();
//
//				if(IsDetails(str).equals(SALARY_MATCHE) || IsDetails(str).equals(BONUS_MATCHE) ) {
//					return IsDetails(str) + "明細" ;
//				}
//			}
//		}
//		return null;
//	}
//
//
//	/**
//	 * 明細書の文字列を判別する
//	 * @param str
//	 * @return
//	 */
//	private  String IsDetails(String str) {
//
//		//明細書と書かれて文字列を検索
//		if(str.contains(SALARY_BONUS_MATCHES)) {
//			//最初の2文字を戻す
//			return str.substring(0,2);
//		}
//		return null;
//	}
//
//
//}
