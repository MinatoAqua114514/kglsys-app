package com.kglsys.web.controller;

import com.kglsys.api.request.UserProfileRequest;
import com.kglsys.api.response.UserProfileResponse;
import com.kglsys.common.util.ApiResponse;
import com.kglsys.application.service.UserProfileService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user-profile")
public class UserProfileController {

    @Autowired
    private UserProfileService userProfileService;


    /**
     * 创建用户个人资料
     * 职责：仅创建新的用户资料，如果已存在则返回错误
     * HTTP方法：POST（创建资源）
     * 路径：POST /api/user-profiles
     */
    @PostMapping
    public ResponseEntity<ApiResponse<UserProfileResponse>> createUserProfile(
            @Valid @RequestBody UserProfileRequest request) {
        try {
            UserProfileResponse profile = userProfileService.createUserProfile(request);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success("个人资料创建成功", profile));
        } catch (IllegalStateException e) {
            // 用户资料已存在
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(ApiResponse.error(409, "创建失败：" + e.getMessage()));
        } catch (IllegalArgumentException e) {
            // 参数错误或用户不存在
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(400, "参数错误：" + e.getMessage()));
        } catch (Exception e) {
            // 其他未预期的错误
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(500, "服务器内部错误"));
        }
    }

    /**
     * 更新用户个人资料
     * 职责：仅更新已存在的用户资料，如果不存在则返回错误
     * HTTP方法：PUT（更新资源）
     * 路径：PUT /api/user-profiles/{userId}
     */
    @PutMapping("/{userId}")
    public ResponseEntity<ApiResponse<UserProfileResponse>> updateUserProfile(
            @PathVariable Long userId,
            @Valid @RequestBody UserProfileRequest request) {
        try {
            // 确保路径参数与请求体中的userId一致
            if (!userId.equals(request.getUserId())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(ApiResponse.error(400, "路径参数userId与请求体中的userId不匹配"));
            }

            UserProfileResponse profile = userProfileService.updateUserProfile(request);
            return ResponseEntity.ok(ApiResponse.success("个人资料更新成功", profile));
        } catch (IllegalStateException e) {
            // 用户资料不存在
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(404, "更新失败：" + e.getMessage()));
        } catch (IllegalArgumentException e) {
            // 参数错误或用户不存在
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(400, "参数错误：" + e.getMessage()));
        } catch (Exception e) {
            // 其他未预期的错误
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(500, "服务器内部错误"));
        }
    }

    /**
     * 保存用户个人资料（创建或更新）
     * 职责：兼容性接口，智能判断是创建还是更新
     * HTTP方法：POST（因为可能创建资源）
     * 路径：POST /api/user-profiles/save
     * <p>
     * 注意：这是一个兼容性方法，建议客户端优先使用明确的创建或更新接口
     */
    @PostMapping("/save")
    public ResponseEntity<ApiResponse<UserProfileResponse>> saveUserProfile(
            @Valid @RequestBody UserProfileRequest request) {
        try {
            boolean profileExists = userProfileService.existsUserProfile(request.getUserId());
            UserProfileResponse profile = userProfileService.saveUserProfile(request);

            String message = profileExists ? "个人资料更新成功" : "个人资料创建成功";
            HttpStatus status = profileExists ? HttpStatus.OK : HttpStatus.CREATED;

            return ResponseEntity.status(status)
                    .body(ApiResponse.success(message, profile));
        } catch (IllegalArgumentException e) {
            // 参数错误或用户不存在
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(400, "保存失败：" + e.getMessage()));
        } catch (Exception e) {
            // 其他未预期的错误
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(500, "服务器内部错误"));
        }
    }

    /**
     * 获取用户个人资料
     * 职责：根据用户ID获取个人资料
     * HTTP方法：GET（读取资源）
     * 路径：GET /api/user-profiles/{userId}
     */
    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<UserProfileResponse>> getUserProfile(@PathVariable Long userId) {
        try {
            // 这里需要实现getUserProfile方法
            UserProfileResponse profile = userProfileService.getUserProfile(userId);
            return ResponseEntity.ok(ApiResponse.success("获取个人资料成功", profile));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(404, "用户资料不存在"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(500, "服务器内部错误"));
        }
    }

    /**
     * 删除用户个人资料
     */
    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponse<Void>> deleteUserProfile(
            @PathVariable Long userId) {
        try {
            userProfileService.deleteUserProfile(userId);
            return ResponseEntity.ok(ApiResponse.success("个人资料删除成功", null));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(404, "删除失败：" + e.getMessage()));
        }
    }

    /**
     * 检查用户个人资料是否存在
     */
    @GetMapping("/{userId}/exists")
    public ResponseEntity<ApiResponse<Boolean>> existsUserProfile(
            @PathVariable Long userId) {
        boolean exists = userProfileService.existsUserProfile(userId);
        return ResponseEntity.ok(ApiResponse.success("检查完成", exists));
    }
}
