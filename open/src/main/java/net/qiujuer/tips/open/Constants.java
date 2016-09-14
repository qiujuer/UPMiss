package net.qiujuer.tips.open;

/**
 * UPMiss Constants
 */
public class Constants {

    public static final String QQ_APP_ID = "1104720590";

    public static final String WX_APP_ID = "wxfb49d2aaf5696365";

    public static final String WX_ACTION = "action";

    public static final String WX_ACTION_INVITE = "invite";

    public static final String WX_RESULT_CODE = "ret";

    public static final String WX_RESULT_MSG = "msg";

    public static final String WX_RESULT = "result";

    public static final String TARGET_URL = "http://www.qiujuer.net";

    public static final String SUMMARY = "\n#UPMiss#，那些重要的记忆～";

    public static final String APP_NAME = "UPMiss";

    public static final String WB_APP_ID = "1239899374";

    public static final String WB_REDIRECT_URL = "https://api.weibo.com/oauth2/default.html";

    /**
     * Scope 是 OAuth2.0 授权机制中 authorize 接口的一个参数。通过 Scope，平台将开放更多的微博
     * 核心功能给开发者，同时也加强用户隐私保护，提升了用户体验，用户在新 OAuth2.0 授权页中有权利
     * 选择赋予应用的功能。
     * <p>
     * 我们通过新浪微博开放平台-->管理中心-->我的应用-->接口管理处，能看到我们目前已有哪些接口的
     * 使用权限，高级权限需要进行申请。
     * <p>
     * 目前 Scope 支持传入多个 Scope 权限，用逗号分隔。
     * <p>
     * 有关哪些 OpenAPI 需要权限申请，请查看：http://open.weibo.com/wiki/%E5%BE%AE%E5%8D%9AAPI
     * 关于 Scope 概念及注意事项，请查看：http://open.weibo.com/wiki/Scope
     */
    public static final String WB_SCOPE =
            "email,direct_messages_read,direct_messages_write,"
                    + "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
                    + "follow_app_official_microblog," + "invitation_write";
}
