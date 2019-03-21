package com.aiwac.cilentapp.patrobot.utils;


import android.util.Log;

import com.aiwac.cilentapp.patrobot.bean.BaseEntity;
import com.aiwac.cilentapp.patrobot.bean.User;
import com.aiwac.cilentapp.patrobot.bean.aVDetail;
import com.aiwac.robotapp.commonlibrary.bean.WifiInfo;
import com.aiwac.robotapp.commonlibrary.common.Constant;
import com.aiwac.robotapp.commonlibrary.exception.JsonException;
import com.aiwac.robotapp.commonlibrary.utils.LogUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.UUID;


import static com.aiwac.robotapp.commonlibrary.common.Constant.WEBSOCKET_LECTURE_VIDEO_ABSTRACT_TYPE_CODE;
import static com.aiwac.robotapp.commonlibrary.common.Constant.WEBSOCKET_MESSAGE_SYSYTEM_CLIENTTYPE;


/**     对象和json直接的相互转换
 * Created by luwang on 2017/10/23.
 */

public class JsonUtil {


    /*     用户 Json 字符串格式
        String jsonStr =
                {
                "opt" : "insert"
                "user":
                    [{"id":1,"number":"15911112222","name":"zhangsan"}]
                }
     */

    //解析json获取操作 ，如插入，更新等
    public static String parseOpt(String jsonStr){
        try{
            JSONObject root = new JSONObject(jsonStr);
            String opt = root.getString(Constant.JSON_OPT);
            LogUtil.d(Constant.JSON_PARSE_SUCCESS + opt);
            return opt;
        }catch (Exception e){
            e.printStackTrace();
            LogUtil.d(Constant.JSON_PARSE_EXCEPTION);
            throw new JsonException(Constant.JSON_PARSE_EXCEPTION, e);
        }
    }

    //解析businessType 获取事物类型
    public static String parseBusinessType(String jsonStr){
        try{
            JSONObject root = new JSONObject(jsonStr);
            String result = root.getString(Constant.WEBSOCKET_MESSAGE_BUSSINESSTYPE);
            return result;
        }catch (Exception e){
            e.printStackTrace();
            LogUtil.d(Constant.JSON_PARSE_EXCEPTION);
            throw new JsonException(Constant.JSON_PARSE_EXCEPTION, e);
        }
    }

    //解析errorCode 获取消息是否成功传递到后台
    public static String parseErrorCode(String jsonStr){
        try{
            JSONObject root = new JSONObject(jsonStr);
            String result = root.getString(Constant.RETURN_JSON_ERRORCODE);
            return result;
        }catch (Exception e){
            e.printStackTrace();
            LogUtil.d(Constant.JSON_PARSE_EXCEPTION);
            throw new JsonException(Constant.JSON_PARSE_EXCEPTION, e);
        }
    }


    //解析errorCode 获取消息是否成功传递到后台
    public static String parseToken(String jsonStr){
        try{
            JSONObject root = new JSONObject(jsonStr);
            String result = root.getString(Constant.USER_DATA_FIELD_TOKEN);
            return result;
        }catch (Exception e){
            e.printStackTrace();
            LogUtil.d(Constant.JSON_PARSE_EXCEPTION);
            throw new JsonException(Constant.JSON_PARSE_EXCEPTION, e);
        }
    }


    //解析ErrorDesc
    public static String parseErrorDesc(String jsonStr){
        try{
            JSONObject root = new JSONObject(jsonStr);
            String result = root.getString(Constant.WEBSOCKET_MESSAGE_ERRORDESC);
            return result;
        }catch (Exception e){
            e.printStackTrace();
            LogUtil.d(Constant.JSON_PARSE_EXCEPTION);
            throw new JsonException(Constant.JSON_PARSE_EXCEPTION, e);
        }
    }

    //解析用户登录密码
    public static String parsePWD(String jsonStr){
        try{
            JSONObject root = new JSONObject(jsonStr);
            String result = root.getString(Constant.USER_DATA_FIELD_PASSWORD);
            return result;
        }catch (Exception e){
            e.printStackTrace();
            LogUtil.d(Constant.JSON_PARSE_EXCEPTION);
            throw new JsonException(Constant.JSON_PARSE_EXCEPTION, e);
        }
    }


    //解析uuid 获取定时任务消息的UUID属性
    public static String parseTimerUUID(String jsonStr){
        try{
            JSONObject root = new JSONObject(jsonStr);
            String result = root.getString(Constant.WEBSOCKET_MESSAGE_UUID);
            return result;
        }catch (Exception e){
            e.printStackTrace();
            LogUtil.d(Constant.JSON_PARSE_EXCEPTION);
            throw new JsonException(Constant.JSON_PARSE_EXCEPTION, e);
        }
    }

    //解析json result等
    public static boolean parseResult(String jsonStr){
        try{
            JSONObject root = new JSONObject(jsonStr);
            String errorCode = root.getString(Constant.WEBSOCKET_TIMER_ERRORCODE);
            return Constant.MESSAGE_ERRORCODE_2000.equals(errorCode);
        }catch (Exception e){
            e.printStackTrace();
            LogUtil.d(Constant.JSON_PARSE_EXCEPTION);
            throw new JsonException(Constant.JSON_PARSE_EXCEPTION, e);
        }
    }

    /**
     *
     * @param jsonStr  json串
     * @param key
     * @return
     */
    public static String parseByKey(String jsonStr, String key){
        try{
            JSONObject root = new JSONObject(jsonStr);
            return root.getString(key);
        }catch (Exception e){
            e.printStackTrace();
            LogUtil.d(Constant.JSON_PARSE_EXCEPTION);
            throw new JsonException(Constant.JSON_PARSE_EXCEPTION, e);
        }
    }

    //将BaseEntity对象转换成json字符串
    public static String baseEntity2Json(BaseEntity baseEntity){
        JSONObject root = new JSONObject();
        try{
            root.put(Constant.WEBSOCKET_MESSAGE_CLIENTID, baseEntity.getClientId());
            root.put(Constant.WEBSOCKET_MESSAGE_BUSSINESSTYPE, baseEntity.getBusinessType());
            root.put(Constant.WEBSOCKET_MESSAGE_UUID, UUID.randomUUID().toString());
            root.put(Constant.WEBSOCKET_MESSAGE_CLIENTTYPE, baseEntity.getClientType());
            root.put(Constant.WEBSOCKET_MESSAGE_TIME, System.currentTimeMillis()+"");
            LogUtil.d(Constant.JSON_GENERATE_SUCCESS + root.toString());
            return root.toString();
        }catch (Exception e){
            e.printStackTrace();
            LogUtil.d(Constant.JSON_GENERATE_EXCEPTION);
            throw new JsonException(Constant.JSON_GENERATE_EXCEPTION, e);
        }
    }

    //通过id查询挂号信息
    public static String queryRegistgerInfoById(BaseEntity baseEntity, String id){
        JSONObject root = new JSONObject();
        try{
            root.put(Constant.WEBSOCKET_MESSAGE_CLIENTID, baseEntity.getClientId());
            root.put(Constant.WEBSOCKET_MESSAGE_BUSSINESSTYPE, baseEntity.getBusinessType());
            root.put(Constant.WEBSOCKET_MESSAGE_UUID, UUID.randomUUID().toString());
            root.put(Constant.WEBSOCKET_MESSAGE_CLIENTTYPE, baseEntity.getClientType());
            root.put(Constant.WEBSOCKET_MESSAGE_TIME, System.currentTimeMillis()+"");
            root.put(Constant.WEBSOCKET_REGISTER_ID, id);
            LogUtil.d(Constant.JSON_GENERATE_SUCCESS + root.toString());
            return root.toString();
        }catch (Exception e){
            e.printStackTrace();
            LogUtil.d(Constant.JSON_GENERATE_EXCEPTION);
            throw new JsonException(Constant.JSON_GENERATE_EXCEPTION, e);
        }
    }


    //解析json获取用户注册是否成功
    public static boolean isUserRegisterSucess(String jsonStr){
        try{
            JSONObject root = new JSONObject(jsonStr);
            String isSuccess = root.getString(Constant.USER_REGISTER_ISSUCCESS);
            LogUtil.d( Constant.JSON_PARSE_SUCCESS + isSuccess);
            return isSuccess.equals(Constant.USER_REGISTER_SUCCESS);
        }catch (Exception e){
            e.printStackTrace();
            LogUtil.d( Constant.JSON_PARSE_EXCEPTION);
            throw new JsonException(Constant.JSON_PARSE_EXCEPTION, e);
        }
    }

    //解析json获取验证码
    public static String parseCheckcode(String jsonStr){
        try{
            JSONObject root = new JSONObject(jsonStr);
            JSONArray jsonArray = root.getJSONArray(Constant.JSON_OBJECT_USER_NAME);
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            String checkcode = jsonObject.getString(Constant.USER_REGISTER_CHECKCODE);
            LogUtil.d( Constant.JSON_PARSE_SUCCESS + checkcode);

            return checkcode;
        }catch (Exception e){
            e.printStackTrace();
            LogUtil.d( Constant.JSON_PARSE_EXCEPTION);
            throw new JsonException(Constant.JSON_PARSE_EXCEPTION, e);
        }
    }

    //解析json 判断用户是否注册
    public static boolean isUserRegisted(String jsonStr){
        try{
            JSONObject root = new JSONObject(jsonStr);
            JSONArray jsonArray = root.getJSONArray(Constant.JSON_OBJECT_USER_NAME);
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            String isNumberExist = jsonObject.getString(Constant.USER_REGISTER_PHONENUMBER_EXIST);
            LogUtil.d( Constant.JSON_PARSE_SUCCESS + isNumberExist);

            return isNumberExist.equals(Constant.USER_REGISTER_PHONENUMBER_EXIST_YES);
        }catch (Exception e){
            e.printStackTrace();
            LogUtil.d( Constant.JSON_PARSE_EXCEPTION);
            throw new JsonException(Constant.JSON_PARSE_EXCEPTION, e);
        }
    }

    //解析json获取用户
    public static User parseJson(String jsonStr) {

        //String jsonStr = "{"id":1,"number":"15911112222","name":"zhangsan"}";
        try {
            User user = new User();
            //将json字符串jsonData装入JSON数组，即JSONArray
            //jsonData可以是从文件中读取，也可以从服务器端获得
            JSONObject root = new JSONObject(jsonStr);

            JSONArray jsonArray = root.getJSONArray(Constant.JSON_OBJECT_USER_NAME);
            // for (int i = 0; i< jsonArray.length(); i++) {
            //循环遍历，依次取出JSONObject对象
            //用getInt和getString方法取出对应键值
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            user.setId(jsonObject.getInt("id"));
            user.setNumber(jsonObject.getString("number"));

            LogUtil.d( Constant.JSON_PARSE_SUCCESS + user.toString());
            // }

            return user;
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.d( Constant.JSON_PARSE_EXCEPTION);
            throw new JsonException(Constant.JSON_PARSE_EXCEPTION, e);
        }
    }

    public static String parseObject(User user, String opt) {
        try{
            JSONObject root = new JSONObject();
            root.put(Constant.JSON_OPT, opt);

            JSONArray jsonArray = new JSONArray();

            JSONObject userJson = new JSONObject();
            userJson.put("id", user.getId());
            userJson.put(Constant.USER_REGISTER_NUMBER, user.getNumber());
            userJson.put("passwd", user.getPassword());
            jsonArray.put(0, userJson);

            root.put(Constant.JSON_OBJECT_USER_NAME, jsonArray);


            LogUtil.d( Constant.JSON_GENERATE_SUCCESS + root.toString());

            return root.toString();
        }catch (Exception e){
            e.printStackTrace();
            LogUtil.d( Constant.JSON_GENERATE_EXCEPTION);
            throw new JsonException(Constant.JSON_GENERATE_EXCEPTION, e);
        }

    }



    public static String setBussinessType(String bussinessType){
        JSONObject root = new JSONObject();
        try{
            root.put(Constant.WEBSOCKET_MESSAGE_BUSSINESSTYPE, bussinessType);

            LogUtil.d( Constant.JSON_GENERATE_SUCCESS + root.toString());
            return root.toString();
        }catch (Exception e){
            e.printStackTrace();
            LogUtil.d( Constant.JSON_GENERATE_EXCEPTION);
            throw new JsonException(Constant.JSON_GENERATE_EXCEPTION, e);
        }
    }

    public static String wifiInfoToJson(WifiInfo wifiInfo){
        JSONObject root = new JSONObject();
        try{
            root.put(Constant.WEBSOCKET_MESSAGE_BUSSINESSTYPE, "0000");
            root.put("ssid", wifiInfo.getSsid());
            root.put("password",wifiInfo.getPassword());
            LogUtil.d( Constant.JSON_GENERATE_SUCCESS + root.toString());
            return root.toString();
        }catch (Exception e){
            e.printStackTrace();
            LogUtil.d( Constant.JSON_GENERATE_EXCEPTION);
            throw new JsonException(Constant.JSON_GENERATE_EXCEPTION, e);
        }
    }


    public static String userToJson(User user){
        JSONObject root = new JSONObject();
        try{
            root.put(Constant.WEBSOCKET_MESSAGE_CLIENTID, user.getClientId());
            root.put(Constant.WEBSOCKET_MESSAGE_BUSSINESSTYPE, user.getBusinessType());
            root.put(Constant.WEBSOCKET_MESSAGE_UUID, user.getUuid());
            root.put(Constant.WEBSOCKET_MESSAGE_CLIENTTYPE, user.getClientType());
            root.put(Constant.WEBSOCKET_MESSAGE_TIME, System.currentTimeMillis()+"");

            LogUtil.d(Constant.JSON_GENERATE_SUCCESS + root.toString());
            return root.toString();
        }catch (Exception e){
            e.printStackTrace();
            LogUtil.d(Constant.JSON_GENERATE_EXCEPTION);
            throw new JsonException(Constant.JSON_GENERATE_EXCEPTION, e);
        }
    }


    public static String queryPersonInfo(BaseEntity baseEntity){
        JSONObject root = new JSONObject();
        try{
            root.put(Constant.WEBSOCKET_MESSAGE_CLIENTID, baseEntity.getClientId());
            root.put(Constant.WEBSOCKET_MESSAGE_BUSSINESSTYPE, baseEntity.getBusinessType());
            root.put(Constant.WEBSOCKET_MESSAGE_UUID, UUID.randomUUID());
            root.put(Constant.WEBSOCKET_MESSAGE_CLIENTTYPE, baseEntity.getClientType());
            root.put(Constant.WEBSOCKET_MESSAGE_TIME, System.currentTimeMillis()+"");

            LogUtil.d(Constant.JSON_GENERATE_SUCCESS + root.toString());
            return root.toString();
        }catch (Exception e){
            e.printStackTrace();
            LogUtil.d(Constant.JSON_GENERATE_EXCEPTION);
            throw new JsonException(Constant.JSON_GENERATE_EXCEPTION, e);
        }
    }

    //生成查询讲座视频摘要的json
    public static String videoAbstract2Json(){
        JSONObject root = new JSONObject();
        try{
            User user = new User();
            root.put(Constant.WEBSOCKET_MESSAGE_ACCOUNT, user.clientId);
            root.put(Constant.WEBSOCKET_MESSAGE_CODE, WEBSOCKET_LECTURE_VIDEO_ABSTRACT_TYPE_CODE);
            root.put(Constant.WEBSOCKET_MESSAGE_UUID, UUID.randomUUID().toString());
            root.put(Constant.WEBSOCKET_MESSAGE_CLIENTTYPE, WEBSOCKET_MESSAGE_SYSYTEM_CLIENTTYPE);
            root.put(Constant.WEBSOCKET_MESSAGE_TIME,System.currentTimeMillis() + "");
            Log.d("make",root.toString());
            LogUtil.d( Constant.JSON_GENERATE_SUCCESS + root.toString());
            return root.toString();
        }catch (Exception e){
            e.printStackTrace();
            LogUtil.d( Constant.JSON_GENERATE_EXCEPTION);
            throw new JsonException(Constant.JSON_GENERATE_EXCEPTION, e);
        }
    }

    //生成查询讲座音频摘要的json
    public static String audioAbstract2Json( ){
        JSONObject root = new JSONObject();
        try{
            User user = new User();
            root.put(Constant.WEBSOCKET_MESSAGE_ACCOUNT, user.clientId);
            root.put(Constant.WEBSOCKET_MESSAGE_CODE, Constant.WEBSOCKET_LECTURE_AUDIO_ABSTRACT_TYPE_CODE);
            root.put(Constant.WEBSOCKET_MESSAGE_UUID, UUID.randomUUID().toString());
            root.put(Constant.WEBSOCKET_MESSAGE_CLIENTTYPE, Constant.WEBSOCKET_MESSAGE_SYSYTEM_CLIENTTYPE);
            root.put(Constant.WEBSOCKET_MESSAGE_TIME,System.currentTimeMillis() + "");
            Log.d("make",root.toString());
            LogUtil.d( Constant.JSON_GENERATE_SUCCESS + root.toString());
            return root.toString();
        }catch (Exception e){
            e.printStackTrace();
            LogUtil.d( Constant.JSON_GENERATE_EXCEPTION);
            throw new JsonException(Constant.JSON_GENERATE_EXCEPTION, e);
        }
    }
    //生成查询讲座音视频详情的json
    public static String aVDetail2Json(String lectureID ){
        JSONObject root = new JSONObject();
        try{
            User user = new User();
            root.put(Constant.WEBSOCKET_MESSAGE_ACCOUNT, user.clientId);
            root.put(Constant.WEBSOCKET_MESSAGE_CODE, Constant.WEBSOCKET_LECTURE_AV_DETAIL_TYPE_CODE);
            root.put(Constant.WEBSOCKET_MESSAGE_UUID, UUID.randomUUID().toString());
            root.put(Constant.WEBSOCKET_MESSAGE_CLIENTTYPE, Constant.WEBSOCKET_MESSAGE_SYSYTEM_CLIENTTYPE);
            root.put(Constant.WEBSOCKET_MESSAGE_TIME,System.currentTimeMillis() + "");
            root.put(Constant.WEBSOCKET_MESSAGE_LECTUREID, lectureID);
            Log.d("make",root.toString());
            LogUtil.d( Constant.JSON_GENERATE_SUCCESS + root.toString());
            return root.toString();
        }catch (Exception e){
            e.printStackTrace();
            LogUtil.d( Constant.JSON_GENERATE_EXCEPTION);
            throw new JsonException(Constant.JSON_GENERATE_EXCEPTION, e);
        }
    }

/*    public static User jsonToPersonInfo(String json){
        User user = new User();
        try{
            JSONObject root = new JSONObject(json);

            return user;
        }catch (Exception e){
            e.printStackTrace();
            LogUtil.d( Constant.JSON_PARSE_EXCEPTION);
            throw new JsonException(Constant.JSON_PARSE_EXCEPTION, e);
        }
    }*/

    //解析json 获取讲座  视音频的详细信息
    public static aVDetail parseLectureAVDetailInfo(String jsonStr){
        String errorCode = JsonUtil.parseErrorCode(jsonStr);
        if(errorCode.equals(Constant.MESSAGE_ERRORCODE_2000)) {

            try {
                JSONObject root = new JSONObject(jsonStr);

                aVDetail  lectureAVDetail = new  aVDetail();

                lectureAVDetail.setLectureID(root.getString(Constant.WEBSOCKET_MESSAGE_ACCOUNT));
                lectureAVDetail.setBusinessType(root.getString(Constant.WEBSOCKET_MESSAGE_CODE));
                lectureAVDetail.setClientType(root.getString(Constant.WEBSOCKET_MESSAGE_CLIENTTYPE));
                lectureAVDetail.setUniqueID(root.getString(Constant.WEBSOCKET_MESSAGE_UUID));

                lectureAVDetail.setLink(root.getString(Constant.WEBSOCKET_MESSAGE_LECTURE_AV_LINK));

                return lectureAVDetail;

            } catch (Exception e) {
                e.printStackTrace();
                Log.d("TAG",Constant.JSON_PARSE_EXCEPTION);
                throw new JsonException(Constant.JSON_PARSE_EXCEPTION, e);
            }
        }else {
            return null;
        }
    }




}
