package tucil_1_stima.util;

import tucil_1_stima.model.Board;
import tucil_1_stima.model.Block;
import java.util.List;

public class PuzzleConfig {
    private String fileName;
    private Board board;
    private List<Block> blocks;
    private Long searchTime;
    private Long casesExamined;

    public PuzzleConfig(String fileName, Board board, List<Block> blocks) {
        this.fileName = fileName;
        this.board = board;
        this.blocks = blocks;
    }
    public void setSearchTime(Long searchTime) {this.searchTime = searchTime;}
    public Long getSearchTime() {return searchTime;}
    public void setCasesExamined(Long casesExamined) {this.casesExamined = casesExamined;}
    public Long getCasesExamined() {return casesExamined;}
    public String getFileName() { return fileName; }
    public Board getBoard() { return board; }
    public List<Block> getBlocks() { return blocks; }
}
