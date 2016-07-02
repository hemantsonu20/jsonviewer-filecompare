package com.github.jutil.compare.parser;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.fife.ui.rsyntaxtextarea.RSyntaxDocument;

import difflib.DiffRow;
import difflib.DiffRowGenerator;
import difflib.DiffUtils;
import difflib.Patch;

public class DiffParser {

    public static final String NEW_LINE = "\n";
    
    // to mark a line is unused
    public static final String INVISIBLE_CHAR = "\r\r";
    private DiffResult diffResult;
    private DiffRowGenerator rowGenerator;

    public DiffParser() {

        diffResult = new DiffResult();
        rowGenerator = getRowGenerator();
    }

    long parseStart = System.currentTimeMillis();

    public DiffResult parse(RSyntaxDocument leftDoc, RSyntaxDocument rightDoc) {

        diffResult.clear();

        try {

            List<String> left = Arrays.asList(leftDoc.getText(0, leftDoc.getLength()).split(NEW_LINE));
            List<String> right = Arrays.asList(rightDoc.getText(0, rightDoc.getLength()).split(NEW_LINE));

            left = left.stream().filter( a -> !a.contains(INVISIBLE_CHAR)).collect(Collectors.toList());
            right = right.stream().filter( a -> !a.contains(INVISIBLE_CHAR)).collect(Collectors.toList());

            Patch<String> patch = DiffUtils.diff(left, right);
            if (!patch.getDeltas().isEmpty()) {

                List<DiffRow> diffRows = rowGenerator.generateDiffRows(left, right, patch);
                diffResult.setDiffRows(diffRows);

//                ObjectMapper mapper = new ObjectMapper();
//                System.out.println(mapper.writeValueAsString(diffRows));
//                System.out.println(mapper.writeValueAsString(patch));
            }

        } catch (Exception e) {
            diffResult.setError(e);
        }

        long parseEnd = System.currentTimeMillis();
        diffResult.setParseTime(parseEnd - parseStart);
        return diffResult;
    }

    private DiffRowGenerator getRowGenerator() {

        DiffRowGenerator.Builder builder = new DiffRowGenerator.Builder();
        builder.showInlineDiffs(false);
        builder.columnWidth(Integer.MAX_VALUE);
        builder.ignoreWhiteSpaces(false);
        builder.defaultString(INVISIBLE_CHAR);
        return builder.build();
    }
}
