package com.mna.aipj.utils;

import com.mna.aipj.dto.AuthorMention;
import com.mna.aipj.dto.exception.UserException;
import org.dhatim.fastexcel.reader.Cell;
import org.dhatim.fastexcel.reader.ReadableWorkbook;
import org.dhatim.fastexcel.reader.Row;
import org.dhatim.fastexcel.reader.Sheet;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

@Component
public class ExcelReader {

    private final int ROW_INFO_START = 8;

    public List<AuthorMention> excelToAuthorMentions(String fileLocation) throws IOException {
        List<AuthorMention> authorMentions;

        try (FileInputStream file = new FileInputStream(fileLocation); ReadableWorkbook wb = new ReadableWorkbook(file)) {
            Sheet sh = wb.getFirstSheet();
            authorMentions = sh.openStream()
                    .filter(row -> row.getRowNum() > ROW_INFO_START)
                    .map(this::rowToAuthorMention)
                    .toList();
        } catch (Exception e) {
            throw new IOException("Error while processing the Excel file", e);
        }

        return authorMentions;
    }

    private AuthorMention rowToAuthorMention(Row row) {
        AuthorMention authorMention = new AuthorMention();

        Map<Integer, Consumer<Cell>> cellActions = new HashMap<>();
        cellActions.put(3, cell -> authorMention.setDate(excelDateToLocalDateTime(cell.asString())));
        cellActions.put(4, cell -> authorMention.setTitle(cell.asString()));
        cellActions.put(5, cell -> authorMention.setSnippet(cell.asString()));
        cellActions.put(6, cell -> authorMention.setFullText(cell.asString()));
        cellActions.put(7, cell -> authorMention.setUrl(cell.asString()));
        cellActions.put(9, cell -> authorMention.setSentiment(cell.asString()));

        int CELL_INIT_IDX = 3;
        int CELL_LAST_IDX = 10;

        row.getCells(CELL_INIT_IDX, CELL_LAST_IDX).stream()
                .filter(cell -> cell.getColumnIndex() != 8)
                .forEach(cell -> {
                    Consumer<Cell> action = cellActions.get(cell.getColumnIndex());
                    if (action != null) {
                        action.accept(cell);
                    } else {
                        throw new UserException(String.format("invalid cell index: %s", cell.getColumnIndex()),
                                HttpStatus.INTERNAL_SERVER_ERROR);
                    }
                });

        return authorMention;
    }

    private LocalDateTime excelDateToLocalDateTime(String dateStr) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
        try {
            return LocalDateTime.parse(dateStr, formatter);
        } catch (DateTimeParseException e) {
            throw new UserException(String.format("Invalid date format: %s", dateStr), HttpStatus.BAD_REQUEST);
        }
    }
}
