package dev.tpcoder.devassist;

import jakarta.annotation.PostConstruct;
import org.springframework.ai.reader.ExtractedTextFormatter;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.reader.pdf.config.PdfDocumentReaderConfig;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class RagWorker {

    private final VectorStore vectorStore;
    private final ApplicationContext appContext;

    public RagWorker(VectorStore vectorStore, ApplicationContext appContext) {
        this.vectorStore = vectorStore;
        this.appContext = appContext;
    }

    @PostConstruct
    public void init() {
//        var extractedTextFormatter = new ExtractedTextFormatter.Builder()
//                .withNumberOfTopTextLinesToDelete(0)
//                .build();
//        var config = PdfDocumentReaderConfig.builder()
//                .withPageTopMargin(0)
//                .withPageExtractedTextFormatter(extractedTextFormatter)
//                .withPagesPerDocument(1)
//                .build();
//        var pdfReader = new PagePdfDocumentReader(pdfResource, config)
        TextReader stackOverflowSurveyInformationReader =
                new TextReader(appContext.getResource("url:https://survey.stackoverflow.co/2023/"));
        TokenTextSplitter tokenTextSplitter = new TokenTextSplitter();
        vectorStore.add(tokenTextSplitter.apply(stackOverflowSurveyInformationReader.get()));
    }
}
