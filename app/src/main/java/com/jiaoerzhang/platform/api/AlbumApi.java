package com.jiaoerzhang.platform.api;/** * 专辑接口 */public class AlbumApi {        public static final String ALBUM_URL = "/api/album/style";    public static final String ALBUM_STYLE_URL = "/api/styles/all";    public static final String ALBUM_DETAIL_URL = "/api/album/{id}";    public static final String MODEL_ALL_URL = "/api/modeling/all";    public static final String CLARITY_ALBUM_ALL_URL = "/api/album/clarity/all";//高清普通专辑    public static final String ALBUM_PHOTOS_URL = "api/photo/all";//照片接口    public static final String COLLECTION_URL = "user/collect/put";//收藏专辑    public static final String CANCEL_COLLECTION_URL = "user/collect/delete";//收藏专辑    public static final String SEARCH_ALBUM_URL = "api/album/style_clarity/all";//搜索专辑    public static final String SEARCH_HOT_URL = "api/user/get-hot-words";//搜索热词}