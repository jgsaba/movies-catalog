package test.raven.moviescatalog.integration.utils;

import java.util.List;

public class PaginatedResponse<T> {

    private List<T> content;
    private boolean last;
    private int totalElements;
    private int totalPages;
    private int number;

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    private int size;

    public List<T> getContent() {
        return content;
    }

    public void setContent(List<T> content) {
        this.content = content;
    }

    public boolean isLast() {
        return last;
    }

    public void setLast(boolean last) {
        this.last = last;
    }

    public int getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(int totalElements) {
        this.totalElements = totalElements;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
