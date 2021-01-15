package pdf;

import java.util.ArrayList;

import pdf.Merge.PdfMergeCreate;
import pdf.Password.pdfPasswordCheck;
import pdf.Split.PdfSplitFilePath;

public class PdfSplitCreate {


	public static void main(String args[]) {


		PdfSplitFilePath psfp = new PdfSplitFilePath();

		pdfPasswordCheck ppc = new pdfPasswordCheck();

		String pdfPath[] ;
		String pass = "";

		ArrayList<String> list = new ArrayList<String>();

		// 分割後のファイルパスおよび振込明細のファイルパスを取得
		list = psfp.SplitCreate(args);

		//パスワードを取得
		pass = ppc.pdfPassCheck(args);

		pdfPath = list.toArray(new String[list.size()]);

		PdfMergeCreate pmc = new PdfMergeCreate();

		pmc.pdfMerge(pdfPath, pass);
	}

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

