package surfy.comfy.controller;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;
import surfy.comfy.config.BaseResponse;
import surfy.comfy.data.post.*;
import surfy.comfy.repository.PostRepository;
import surfy.comfy.service.BookmarkService;
import surfy.comfy.service.PostService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final BookmarkService bookmarkService;

    private final Logger logger= LoggerFactory.getLogger(PostController.class);

    /**
     * minseo
     * 마이 페이지 조회
     * @param memberId
     * @return MyPageResponse
     */
    @GetMapping("/myPage/{memberId}")
    public BaseResponse<MyPageResponse> myPage(@PathVariable(name="memberId")Long memberId) {
        logger.info("mypage - memberId: {}",memberId);
        List<GetPostResponse> myPostList = postService.getMyposts(memberId);
        List<GetPostResponse> bookmarkList = bookmarkService.getBookmarks(memberId);
        logger.info("bookmarkList: {}",bookmarkList.size());
        MyPageResponse result = new MyPageResponse();
        result.setBookmarks(bookmarkList);
        result.setMyposts(myPostList);

        return new BaseResponse<>(result);
    }

    /**
     * minseo
     * 커뮤니티 조회 v2
     */
    @GetMapping("/community/{memberId}")
    public BaseResponse<List<GetPostResponse>> community(@PathVariable(name="memberId") Long memberId){
        logger.info("[GET] - /community");
        List<GetPostResponse> result=postService.getAllPosts(memberId);

        return new BaseResponse<>(result);
    }

    /**
     * minseo
     * 게시글 조회
     * @param postId
     * @return memberId
     */

    @GetMapping("/post/{postId}/{memberId}")
    public BaseResponse<GetPostResponse> viewPost(@PathVariable(name="postId")Long postId,@PathVariable(name="memberId")Long memberId){
        logger.info("[viewPost] - request: {}",memberId);
        GetPostResponse getPostResponse=postService.getPost(postId, memberId);

        return new BaseResponse<>(getPostResponse);
    }



    /**
     * 게시글 삭제
     * @param postId
     * @param memberId
     * @return
     */
    @DeleteMapping("/post/{postId}/{memberId}")
    public BaseResponse<DeletePostResponse> deletePost(@PathVariable(name="postId")Long postId,@PathVariable(name="memberId")String memberId){
        logger.info("[deletePost]: {}",postId);
        DeletePostResponse response= postService.deletePost(postId,memberId);
        logger.info("[deletePost]: {}",response);
        return new BaseResponse<>(response);
    }

    /**
     * 게시글 제목으로 검색
     * @param title
     * @return
     */
    @GetMapping("/search")
    public BaseResponse<List<PostResponse>> Search(String title){
        List<PostResponse> SearchList = postService.searchPost(title);
        System.out.println("title:"+ title);
        System.out.println("search:"+ SearchList);
        return new BaseResponse<>(SearchList);

    }
}
