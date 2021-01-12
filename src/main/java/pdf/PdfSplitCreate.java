package pdf;

import java.util.ArrayList;

import PdfMarge.PdfMargeCreate;
import pdf.Split.PdfSplitFilePath;

public class PdfSplitCreate {


	public static ArrayList<String> list = new ArrayList<String>();


	public static void main(String args[]) {


		PdfSplitFilePath psfp = new PdfSplitFilePath();

		String pdfPath[] ;
		String pass = "";

		// 分割後のファイルパスおよび振込明細のファイルパスを取得
		list = psfp.SplitCreate(args);
		pass = psfp.strPass;

		pdfPath = list.toArray(new String[list.size()]);

		//String[] pdfPaths = pdfFilePathSort(pdfPath);

		PdfMargeCreate pmc = new PdfMargeCreate();

		pmc.pdfMarge(pdfPath, pass);
	}



//	public static String[] pdfFilePathSort(String[] pdfPath) {
//
//		String[] fileList = new String[pdfPath.length];
//
//		String[] sbMatch = new String[]{"\\給与明細","\\賞与明細"};
//
//		String[] temp = new String[pdfPath.length];
//		int count = 0;
//
//		String hurikomi = null;
//
//
//		for(String s: sbMatch) {
//
//			for(int j = 0; j < pdfPath.length; j++) {
//				if(pdfPath[j].contains("\\給与明細") || pdfPath[j].contains("\\賞与明細")) {
//					if(pdfPath[j].contains(s)) {
//						System.out.println(count);
//						temp[count] = pdfPath[j];
//						break;
//					}
//
//				}else {
//					System.out.println(count);
//					hurikomi = pdfPath[j];
//					break;
//				}
//
//			}
//			count++;
//
//		}
//
//		for(int i = 0; i < temp.length;i++) {
//
//		}
//
//		return temp;
//
//
//
//	}
}
