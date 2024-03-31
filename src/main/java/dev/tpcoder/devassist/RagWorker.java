package dev.tpcoder.devassist;

import jakarta.annotation.PostConstruct;
import org.springframework.ai.reader.ExtractedTextFormatter;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.reader.pdf.config.PdfDocumentReaderConfig;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

@Component
public class RagWorker {
    private final VectorStore vectorStore;

    private final Resource pdfResource;

    public RagWorker(VectorStore vectorStore,
                     @Value("classpath:2023-infoq-trends-reports.pdf") String infoqTrendsReports) {
        this.vectorStore = vectorStore;
        this.pdfResource = new ClassPathResource(infoqTrendsReports);
    }

    @PostConstruct
    public void init() {
        var extractedTextFormatter = new ExtractedTextFormatter.Builder().build();
        var config = PdfDocumentReaderConfig.builder()
                .withPageExtractedTextFormatter(extractedTextFormatter)
                .build();
        var pdfReader = new PagePdfDocumentReader(pdfResource, config);
        TokenTextSplitter tokenTextSplitter = new TokenTextSplitter();
        vectorStore.accept(tokenTextSplitter.apply(pdfReader.get()));
    }
}
