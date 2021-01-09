package com.gabriel.hwman.screens;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.myfirstapp.R;
import com.shockwave.pdfium.PdfDocument;

import java.util.List;

public class PDFActivity extends Activity implements OnPageChangeListener, OnLoadCompleteListener {
    private static final String TAG = PDFActivity.class.getSimpleName();
    public static String fileName = "test.pdf";
    Integer pageNumber = 0;
    PDFView pdfView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf);
        pdfView = findViewById(R.id.pdfView);

        Intent intent = getIntent();
        String page = intent.getStringExtra("EXTRA_PAGE");
        String book = intent.getStringExtra("EXTRA_BOOK").toLowerCase();
        fileName = book + ".pdf";
        pageNumber = Integer.parseInt(page) + 1;
        displayFromAsset(fileName);
    }

    @Override
    public void onPageChanged(int page, int pageCount) {
        pageNumber = page;
        setTitle(String.format("%s %s / %s", fileName, page + 1, pageCount));
    }


    @Override
    public void loadComplete(int nbPages) {
        PdfDocument.Meta meta = pdfView.getDocumentMeta();
        printBookmarksTree(pdfView.getTableOfContents(), "-");

    }

    private void displayFromAsset(String assetFileName) {
        fileName = assetFileName;

        pdfView.fromAsset(fileName)
                .defaultPage(pageNumber)
                .enableSwipe(true)

                .swipeHorizontal(false)
                .onPageChange(this)
                .enableAnnotationRendering(true)
                .onLoad(this)
                .scrollHandle(new DefaultScrollHandle(this))
                .load();
    }

    public void printBookmarksTree(List<PdfDocument.Bookmark> tree, String sep) {
        for (PdfDocument.Bookmark b : tree) {

            Log.e(TAG, String.format("%s %s, p %d", sep, b.getTitle(), b.getPageIdx()));

            if (b.hasChildren()) {
                printBookmarksTree(b.getChildren(), sep + "-");
            }
        }
    }

}