package surfy.comfy.controller;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import surfy.comfy.config.BaseResponse;
import surfy.comfy.data.bookmark.PostBookmarkRequest;
import surfy.comfy.service.BookmarkService;

@RestController
@RequiredArgsConstructor
public class BookmarkController {

    private final BookmarkService bookmarkService;
    private final Logger logger= LoggerFactory.getLogger(BookmarkController.class);

    /**
     * 즐겨찾기 추가
     */
    @PostMapping("/bookmark")
    public BaseResponse<String> addBookmark(@RequestBody PostBookmarkRequest request){
        logger.info("[addBookmark]- postId: {} memberId: {}",request.getPostId(),request.getMemberId());
        String response=bookmarkService.addBookmark(request.getPostId(),request.getMemberId());

        return new BaseResponse<>(response);
    }
    @DeleteMapping("/bookmark/{postId}/{memberId}")
    public BaseResponse<String> deleteBookmark(@PathVariable(name="postId") Long postId, @PathVariable(name="memberId") Long memberId){
        logger.info("[deleteBookmark]");
        String response=bookmarkService.deleteBookmark(postId,memberId);

        return new BaseResponse<>(response);
    }


}
