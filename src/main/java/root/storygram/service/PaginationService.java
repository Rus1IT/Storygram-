package root.storygram.service;

import org.springframework.data.domain.Page;
import root.storygram.dto.response.PaginationMetaResponse;
import root.storygram.exception.NotFoundException;

public class PaginationService {

    public static PaginationMetaResponse toPaginationMetaResponse(Page<?> page){
        return PaginationMetaResponse.builder()
                .currentPage(page.getNumber())
                .pageSize(page.getSize())
                .totalPages(page.getTotalPages())
                .totalElements(page.getTotalElements())
                .hasNext(page.hasNext())
                .hasPrevious(page.hasPrevious())
                .build();
    }

    public static void validatePage(Page<?> page){
        if (page.getNumber() >= page.getTotalPages()) {
            throw new NotFoundException("Page not found", "page");
        }
    }
}
