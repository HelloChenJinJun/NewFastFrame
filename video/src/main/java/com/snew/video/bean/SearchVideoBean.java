package com.snew.video.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2018/12/14     20:34
 */
public class SearchVideoBean implements Serializable {


    /**
     * head : {"ab_result":"1073741824","error":0,"key":"你好","mix":1,"num":10,"qid":"-Cz01gjDSANfUmgTNaAeGOftY9qWxW4mLTBWuRlNIu48JXYev6dNqQ","report":"action=105&platform=10201&qid=%2DCz01gjDSANfUmgTNaAeGOftY9qWxW4mLTBWuRlNIu48JXYev6dNqQ&srh_platform=2&ival1=0&ival2=7&ival7=11&sval1=%E4%BD%A0%E5%A5%BD&sval2=&sval3=&sval8=145_&sval4=13018129196934178682+4829128812601920753+3014707594216537690+14048088029966981454+3814984672597030378+4872936086020480038+7240110841859835766+6299957369886465477+8833120626146719341+4523549197354031742","result_type":"251"}
     * item : [{"ar":"内地","class":"电影","da":2018,"dc":"http://i.gtimg.cn/qqlive/img/jpgcache/files/qqvideo/hori/c/cbs8caltnjlb6t8.jpg","direct_report":"action=101&platform=10201&qid=%2DCz01gjDSANfUmgTNaAeGOftY9qWxW4mLTBWuRlNIu48JXYev6dNqQ&srh_platform=2&ival1=1&ival2=1&ival7=11&sval1=%E4%BD%A0%E5%A5%BD&sval2=%E4%BD%A0%E5%A5%BD%2C%E4%B9%8B%E5%8D%8E&sval3=1&sval8=145_&sval4=13018129196934178682+4829128812601920753+3014707594216537690+14048088029966981454+3814984672597030378+4872936086020480038+7240110841859835766+6299957369886465477+8833120626146719341+4523549197354031742","ee":0,"et":"Last Letter","ex":{"typeid":1},"id":"cbs8caltnjlb6t8","idType":2,"imgtag":"{\"tag_1\":{\"id\":\"10012\",\"param\":\"mark_vip_free\",\"param2\":\"\",\"text\":\"会员免费\"},\"tag_2\":{\"id\":\"-1\",\"param\":\"\",\"param2\":\"\",\"text\":\"\"},\"tag_3\":{\"id\":\"-1\",\"param\":\"\",\"param2\":\"\",\"text\":\"\"},\"tag_4\":{\"id\":\"40003\",\"param\":\"mark_sd\",\"param2\":\"\",\"text\":\"蓝光\"}}","itemType":1,"markLabelList":"","pa":"周迅 杜江 秦昊","pd":"岩井俊二","ps":1,"report":"action=101&platform=10201&qid=%2DCz01gjDSANfUmgTNaAeGOftY9qWxW4mLTBWuRlNIu48JXYev6dNqQ&srh_platform=2&ival1=1&ival2=7&ival7=11&sval1=%E4%BD%A0%E5%A5%BD&sval2=%E4%BD%A0%E5%A5%BD%2C%E4%B9%8B%E5%8D%8E&sval3=13018129196934178682&sval8=145_&sval4=13018129196934178682+4829128812601920753+3014707594216537690+14048088029966981454+3814984672597030378+4872936086020480038+7240110841859835766+6299957369886465477+8833120626146719341+4523549197354031742","sn":"爱情 剧情 院线","ss":1,"te":0,"title":"<em  class=\"c_txt3\">你好<\/em>,之华","tt":"你好，之华","url":"https://v.qq.com/x/cover/cbs8caltnjlb6t8.html","word":"你好,之华"},{"class":"电视剧","et":"My Huckleberry Friends","ex":{"title":"","typeid":2},"id":"sdp001wd6mt2rmi","idType":2,"itemType":1,"markLabelList":[{"cssText":"","markImageUrl":"http://i.gtimg.cn/qqlive/images/mark/mark_2.png","position":1,"primeText":"预告","type":2}],"ps":0,"report":"action=101&platform=10201&qid=%2DCz01gjDSANfUmgTNaAeGOftY9qWxW4mLTBWuRlNIu48JXYev6dNqQ&srh_platform=2&ival1=1&ival2=7&ival7=11&sval1=%E4%BD%A0%E5%A5%BD&sval2=%E4%BD%A0%E5%A5%BD%2C%E6%97%A7%E6%97%B6%E5%85%89&sval3=4829128812601920753&sval8=145_&sval4=13018129196934178682+4829128812601920753+3014707594216537690+14048088029966981454+3814984672597030378+4872936086020480038+7240110841859835766+6299957369886465477+8833120626146719341+4523549197354031742","title":"<em  class=\"c_txt3\">你好<\/em>,旧时光","url":"https://v.qq.com/x/cover/sdp001wd6mt2rmi.html","word":"你好,旧时光"},{"ar":"内地","class":"电视剧","da":2016,"dc":"http://i.gtimg.cn/qqlive/img/jpgcache/files/qqvideo/hori/m/mvjfcdfi7o83fbj.jpg","direct_report":"action=101&platform=10201&qid=%2DCz01gjDSANfUmgTNaAeGOftY9qWxW4mLTBWuRlNIu48JXYev6dNqQ&srh_platform=2&ival1=1&ival2=1&ival7=11&sval1=%E4%BD%A0%E5%A5%BD&sval2=%E4%BD%A0%E5%A5%BD%E4%B9%94%E5%AE%89&sval3=1&sval8=145_&sval4=13018129196934178682+4829128812601920753+3014707594216537690+14048088029966981454+3814984672597030378+4872936086020480038+7240110841859835766+6299957369886465477+8833120626146719341+4523549197354031742","ee":32,"et":"Hello Joann","ex":{"title":"全32集","typeid":2},"id":"mvjfcdfi7o83fbj","idType":2,"imgtag":"{\"tag_1\":{\"id\":\"-1\",\"param\":\"\",\"param2\":\"\",\"text\":\"\"},\"tag_2\":{\"id\":\"-1\",\"param\":\"\",\"param2\":\"\",\"text\":\"\"},\"tag_3\":{\"id\":\"30001\",\"param\":\"figure_mask\",\"param2\":\"\",\"text\":\"全32集\"},\"tag_4\":{\"id\":\"40003\",\"param\":\"mark_sd\",\"param2\":\"\",\"text\":\"蓝光\"}}","itemType":1,"markLabelList":"","pa":"戚薇 王晓晨 陈亦飞","pd":"林妍","ps":1,"report":"action=101&platform=10201&qid=%2DCz01gjDSANfUmgTNaAeGOftY9qWxW4mLTBWuRlNIu48JXYev6dNqQ&srh_platform=2&ival1=1&ival2=7&ival7=11&sval1=%E4%BD%A0%E5%A5%BD&sval2=%E4%BD%A0%E5%A5%BD%E4%B9%94%E5%AE%89&sval3=3014707594216537690&sval8=145_&sval4=13018129196934178682+4829128812601920753+3014707594216537690+14048088029966981454+3814984672597030378+4872936086020480038+7240110841859835766+6299957369886465477+8833120626146719341+4523549197354031742","sn":"都市 剧情 ","ss":1,"te":32,"title":"<em  class=\"c_txt3\">你好<\/em>乔安","tt":"你好乔安","url":"https://v.qq.com/x/cover/mvjfcdfi7o83fbj.html","word":"你好乔安"},{"ar":"内地","class":"电影","da":2016,"dc":"http://i.gtimg.cn/qqlive/img/jpgcache/files/qqvideo/hori/i/iqswlhxzjslxt66.jpg","direct_report":"action=101&platform=10201&qid=%2DCz01gjDSANfUmgTNaAeGOftY9qWxW4mLTBWuRlNIu48JXYev6dNqQ&srh_platform=2&ival1=1&ival2=1&ival7=11&sval1=%E4%BD%A0%E5%A5%BD&sval2=%E4%BD%A0%E5%A5%BD%2C%E7%96%AF%E5%AD%90!&sval3=1&sval8=145_&sval4=13018129196934178682+4829128812601920753+3014707594216537690+14048088029966981454+3814984672597030378+4872936086020480038+7240110841859835766+6299957369886465477+8833120626146719341+4523549197354031742","ee":0,"et":"The Insanity","ex":{"typeid":1},"id":"iqswlhxzjslxt66","idType":2,"imgtag":"{\"tag_1\":{\"id\":\"-1\",\"param\":\"\",\"param2\":\"\",\"text\":\"\"},\"tag_2\":{\"id\":\"-1\",\"param\":\"\",\"param2\":\"\",\"text\":\"\"},\"tag_3\":{\"id\":\"-1\",\"param\":\"\",\"param2\":\"\",\"text\":\"\"},\"tag_4\":{\"id\":\"40003\",\"param\":\"mark_sd\",\"param2\":\"\",\"text\":\"蓝光\"}}","itemType":1,"markLabelList":"","pa":"万茜 王自健 周一围","pd":"饶晓志","ps":1,"report":"action=101&platform=10201&qid=%2DCz01gjDSANfUmgTNaAeGOftY9qWxW4mLTBWuRlNIu48JXYev6dNqQ&srh_platform=2&ival1=1&ival2=7&ival7=11&sval1=%E4%BD%A0%E5%A5%BD&sval2=%E4%BD%A0%E5%A5%BD%2C%E7%96%AF%E5%AD%90!&sval3=14048088029966981454&sval8=145_&sval4=13018129196934178682+4829128812601920753+3014707594216537690+14048088029966981454+3814984672597030378+4872936086020480038+7240110841859835766+6299957369886465477+8833120626146719341+4523549197354031742","sn":"喜剧 悬疑 院线","ss":1,"te":0,"title":"<em  class=\"c_txt3\">你好<\/em>,疯子!","tt":"你好，疯子！","url":"https://v.qq.com/x/cover/iqswlhxzjslxt66.html","word":"你好,疯子!"},{"ar":"内地","class":"电影","da":2011,"dc":"http://i.gtimg.cn/qqlive/img/jpgcache/files/qqvideo/hori/g/g6055xnwc815rrr.jpg","direct_report":"action=101&platform=10201&qid=%2DCz01gjDSANfUmgTNaAeGOftY9qWxW4mLTBWuRlNIu48JXYev6dNqQ&srh_platform=2&ival1=1&ival2=1&ival7=11&sval1=%E4%BD%A0%E5%A5%BD&sval2=hello%20%E6%A0%91%E5%85%88%E7%94%9F&sval3=1&sval8=145_&sval4=13018129196934178682+4829128812601920753+3014707594216537690+14048088029966981454+3814984672597030378+4872936086020480038+7240110841859835766+6299957369886465477+8833120626146719341+4523549197354031742","ee":0,"et":"Mr. Tree","ex":{"typeid":1},"id":"g6055xnwc815rrr","idType":2,"imgtag":"{\"tag_1\":{\"id\":\"10012\",\"param\":\"mark_vip_free\",\"param2\":\"\",\"text\":\"会员免费\"},\"tag_2\":{\"id\":\"-1\",\"param\":\"\",\"param2\":\"\",\"text\":\"\"},\"tag_3\":{\"id\":\"-1\",\"param\":\"\",\"param2\":\"\",\"text\":\"\"},\"tag_4\":{\"id\":\"40003\",\"param\":\"mark_sd\",\"param2\":\"\",\"text\":\"蓝光\"}}","itemType":1,"markLabelList":"","pa":"王宝强 谭卓 何洁","pd":"韩杰","ps":1,"report":"action=101&platform=10201&qid=%2DCz01gjDSANfUmgTNaAeGOftY9qWxW4mLTBWuRlNIu48JXYev6dNqQ&srh_platform=2&ival1=1&ival2=7&ival7=11&sval1=%E4%BD%A0%E5%A5%BD&sval2=hello%20%E6%A0%91%E5%85%88%E7%94%9F&sval3=3814984672597030378&sval8=145_&sval4=13018129196934178682+4829128812601920753+3014707594216537690+14048088029966981454+3814984672597030378+4872936086020480038+7240110841859835766+6299957369886465477+8833120626146719341+4523549197354031742","sn":"剧情 喜剧 ","ss":1,"te":0,"title":"hello 树先生","tt":"hello 树先生","url":"https://v.qq.com/x/cover/g6055xnwc815rrr.html","word":"hello 树先生"},{"ar":"泰国","class":"电影","da":2010,"dc":"http://i.gtimg.cn/qqlive/img/jpgcache/files/qqvideo/hori/i/i7ytki1yb45czqb.jpg","direct_report":"action=101&platform=10201&qid=%2DCz01gjDSANfUmgTNaAeGOftY9qWxW4mLTBWuRlNIu48JXYev6dNqQ&srh_platform=2&ival1=1&ival2=1&ival7=11&sval1=%E4%BD%A0%E5%A5%BD&sval2=%E4%BD%A0%E5%A5%BD%2C%E9%99%8C%E7%94%9F%E4%BA%BA&sval3=1&sval8=145_&sval4=13018129196934178682+4829128812601920753+3014707594216537690+14048088029966981454+3814984672597030378+4872936086020480038+7240110841859835766+6299957369886465477+8833120626146719341+4523549197354031742","ee":0,"et":"Hello Stranger","ex":{"typeid":1},"id":"i7ytki1yb45czqb","idType":2,"imgtag":"{\"tag_1\":{\"id\":\"-1\",\"param\":\"\",\"param2\":\"\",\"text\":\"\"},\"tag_2\":{\"id\":\"-1\",\"param\":\"\",\"param2\":\"\",\"text\":\"\"},\"tag_3\":{\"id\":\"-1\",\"param\":\"\",\"param2\":\"\",\"text\":\"\"},\"tag_4\":{\"id\":\"40002\",\"param\":\"mark_sd\",\"param2\":\"\",\"text\":\"超清\"}}","itemType":1,"markLabelList":"","pa":"辰塔维·塔纳西维 努娜·能提妲·索彭","pd":"班庄·比辛达拿刚","ps":1,"report":"action=101&platform=10201&qid=%2DCz01gjDSANfUmgTNaAeGOftY9qWxW4mLTBWuRlNIu48JXYev6dNqQ&srh_platform=2&ival1=1&ival2=7&ival7=11&sval1=%E4%BD%A0%E5%A5%BD&sval2=%E4%BD%A0%E5%A5%BD%2C%E9%99%8C%E7%94%9F%E4%BA%BA&sval3=4872936086020480038&sval8=145_&sval4=13018129196934178682+4829128812601920753+3014707594216537690+14048088029966981454+3814984672597030378+4872936086020480038+7240110841859835766+6299957369886465477+8833120626146719341+4523549197354031742","sn":"爱情 喜剧 ","ss":1,"te":0,"title":"<em  class=\"c_txt3\">你好<\/em>,陌生人","tt":"你好，陌生人","url":"https://v.qq.com/x/cover/i7ytki1yb45czqb.html","word":"你好,陌生人"},{"class":"娱乐","ec":"0","ex":{"brief":"","byname":"第12期：路人乱扔水瓶明星啥反应？韩雪一个举动圈粉李亚鹏秀神操","date":"2018-07-1000:00:00","id":"aagy9di61qogxs4","pic":"http://i.gtimg.cn/qqlive/img/jpgcache/files/qqvideo/hori/a/aagy9di61qogxs4.jpg","pl":[{"en":"2018-07-10","id":"aagy9di61qogxs4","ti":"第12期：路人乱扔水瓶明星啥反应？韩雪一个举动圈粉李亚鹏秀神操作","u":"http://v.qq.com/x/cover/aagy9di61qogxs4.html?vid=b07160nvs4i"},{"en":"2018-07-03","id":"ese548a4beyvvjs","ti":"第11期：宋茜偶遇聋哑人问路引导测试员求助附近店铺","u":"http://v.qq.com/x/cover/ese548a4beyvvjs.html?vid=p070965v75q"}],"title":"你好！陌生人第12期","typeid":5},"id":"5_72714","idType":3,"itemType":1,"markLabelList":"","ps":0,"report":"action=101&platform=10201&qid=%2DCz01gjDSANfUmgTNaAeGOftY9qWxW4mLTBWuRlNIu48JXYev6dNqQ&srh_platform=2&ival1=1&ival2=7&ival7=11&sval1=%E4%BD%A0%E5%A5%BD&sval2=%E4%BD%A0%E5%A5%BD!%E9%99%8C%E7%94%9F%E4%BA%BA&sval3=7240110841859835766&sval8=145_&sval4=13018129196934178682+4829128812601920753+3014707594216537690+14048088029966981454+3814984672597030378+4872936086020480038+7240110841859835766+6299957369886465477+8833120626146719341+4523549197354031742","title":"<em  class=\"c_txt3\">你好<\/em>!陌生人","tt":"你好！陌生人","url":"http://v.qq.com/ent/type/5_72714.html","word":"你好!陌生人"},{"class":"综艺","dc":"http://i.gtimg.cn/qqlive/images/newcolumn/v2/p/p12425.jpg","direct_report":"action=101&platform=10201&qid=%2DCz01gjDSANfUmgTNaAeGOftY9qWxW4mLTBWuRlNIu48JXYev6dNqQ&srh_platform=2&ival1=1&ival2=1&ival7=11&sval1=%E4%BD%A0%E5%A5%BD&sval2=%E4%BD%A0%E5%A5%BD%E9%82%BB%E5%B1%85&sval3=1&sval8=145_&sval4=13018129196934178682+4829128812601920753+3014707594216537690+14048088029966981454+3814984672597030378+4872936086020480038+7240110841859835766+6299957369886465477+8833120626146719341+4523549197354031742","ec":"0","ee":20161230,"ex":{"brief":"","byname":"破冰测试\u2014\u2014借针线","date":"2016-12-3000:00:00","id":"glctkeo4qu9nkvj","pic":"http://puui.qpic.cn/vcolumn_pic/0/p12425t1469541683.jpg/0","pl":[{"en":"2016-12-30","new":"0","ti":"破冰测试\u2014\u2014借针线","u":"http://v.qq.com/x/cover/glctkeo4qu9nkvj.html?vid=y0022ing7ih"},{"en":"2016-12-23","new":"0","ti":"明明惨被热心大叔\u201c教育\u201d","u":"http://v.qq.com/x/cover/ajjns0lu8btwizb.html?vid=c0022p73xbn"}],"title":"","typeid":10},"id":"10_51709","idType":3,"itemType":1,"markLabelList":"","ps":1,"report":"action=101&platform=10201&qid=%2DCz01gjDSANfUmgTNaAeGOftY9qWxW4mLTBWuRlNIu48JXYev6dNqQ&srh_platform=2&ival1=1&ival2=7&ival7=11&sval1=%E4%BD%A0%E5%A5%BD&sval2=%E4%BD%A0%E5%A5%BD%E9%82%BB%E5%B1%85&sval3=6299957369886465477&sval8=145_&sval4=13018129196934178682+4829128812601920753+3014707594216537690+14048088029966981454+3814984672597030378+4872936086020480038+7240110841859835766+6299957369886465477+8833120626146719341+4523549197354031742","ss":1,"title":"<em  class=\"c_txt3\">你好<\/em>邻居","tt":"你好邻居","url":"https://v.qq.com/detail/5/51709.html","word":"你好邻居"},{"class":"综艺","dc":"http://i.gtimg.cn/qqlive/images/newcolumn/v2/j/je3ji4.jpg","direct_report":"action=101&platform=10201&qid=%2DCz01gjDSANfUmgTNaAeGOftY9qWxW4mLTBWuRlNIu48JXYev6dNqQ&srh_platform=2&ival1=1&ival2=1&ival7=11&sval1=%E4%BD%A0%E5%A5%BD&sval2=%E4%BD%A0%E5%A5%BD!%E9%9D%A2%E8%AF%95%E5%AE%98&sval3=1&sval8=145_&sval4=13018129196934178682+4829128812601920753+3014707594216537690+14048088029966981454+3814984672597030378+4872936086020480038+7240110841859835766+6299957369886465477+8833120626146719341+4523549197354031742","ec":"1","ee":20180906,"ex":{"brief":"","byname":"高跨度年薪为何导致求职者接连踩雷？","date":"2018-09-0600:00:00","id":"5vkf4jx9yniv25o","pic":"http://puui.qpic.cn/vcolumn_pic/0/je3ji41512041677/0","pl":[{"en":"2018-09-06","new":"0","ti":"高跨度年薪为何导致求职者接连踩雷？","u":"http://v.qq.com/x/cover/5vkf4jx9yniv25o.html?vid=e0027hfdf5z"},{"en":"2018-08-30","new":"0","ti":"30岁海归硕士求蜕变 引发对\u201c三十而立\u201d的质疑","u":"http://v.qq.com/x/cover/kfyyz0yc41e1v8m.html?vid=m0027bppr4g"}],"title":"","typeid":10},"id":"10_71594","idType":3,"itemType":1,"markLabelList":"","ps":1,"report":"action=101&platform=10201&qid=%2DCz01gjDSANfUmgTNaAeGOftY9qWxW4mLTBWuRlNIu48JXYev6dNqQ&srh_platform=2&ival1=1&ival2=7&ival7=11&sval1=%E4%BD%A0%E5%A5%BD&sval2=%E4%BD%A0%E5%A5%BD!%E9%9D%A2%E8%AF%95%E5%AE%98&sval3=8833120626146719341&sval8=145_&sval4=13018129196934178682+4829128812601920753+3014707594216537690+14048088029966981454+3814984672597030378+4872936086020480038+7240110841859835766+6299957369886465477+8833120626146719341+4523549197354031742","ss":1,"title":"<em  class=\"c_txt3\">你好<\/em>!面试官","tt":"你好！面试官","url":"https://v.qq.com/detail/7/71594.html","word":"你好!面试官"},{"class":"电影","et":"Hello, Neighbour!","ex":{"typeid":1},"id":"sdp00165qxvrr2c","idType":2,"itemType":1,"markLabelList":[{"cssText":"","markImageUrl":"http://i.gtimg.cn/qqlive/images/mark/mark_2.png","position":1,"primeText":"预告","type":2}],"ps":0,"report":"action=101&platform=10201&qid=%2DCz01gjDSANfUmgTNaAeGOftY9qWxW4mLTBWuRlNIu48JXYev6dNqQ&srh_platform=2&ival1=1&ival2=7&ival7=11&sval1=%E4%BD%A0%E5%A5%BD&sval2=%E4%BD%A0%E5%A5%BD%2C%E9%82%BB%E5%B1%85!&sval3=4523549197354031742&sval8=145_&sval4=13018129196934178682+4829128812601920753+3014707594216537690+14048088029966981454+3814984672597030378+4872936086020480038+7240110841859835766+6299957369886465477+8833120626146719341+4523549197354031742","title":"<em  class=\"c_txt3\">你好<\/em>,邻居!","url":"https://v.qq.com/x/cover/sdp00165qxvrr2c.html","word":"你好,邻居!"}]
     * result : {"code":0,"msg":""}
     */

    private HeadBean head;
    private ResultBean result;
    private List<ItemBean> item;

    public HeadBean getHead() {
        return head;
    }

    public void setHead(HeadBean head) {
        this.head = head;
    }

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public List<ItemBean> getItem() {
        return item;
    }

    public void setItem(List<ItemBean> item) {
        this.item = item;
    }

    public static class HeadBean {
        /**
         * ab_result : 1073741824
         * error : 0
         * key : 你好
         * mix : 1
         * num : 10
         * qid : -Cz01gjDSANfUmgTNaAeGOftY9qWxW4mLTBWuRlNIu48JXYev6dNqQ
         * report : action=105&platform=10201&qid=%2DCz01gjDSANfUmgTNaAeGOftY9qWxW4mLTBWuRlNIu48JXYev6dNqQ&srh_platform=2&ival1=0&ival2=7&ival7=11&sval1=%E4%BD%A0%E5%A5%BD&sval2=&sval3=&sval8=145_&sval4=13018129196934178682+4829128812601920753+3014707594216537690+14048088029966981454+3814984672597030378+4872936086020480038+7240110841859835766+6299957369886465477+8833120626146719341+4523549197354031742
         * result_type : 251
         */

        private String ab_result;
        private int error;
        private String key;
        private int mix;
        private int num;
        private String qid;
        private String report;
        private String result_type;

        public String getAb_result() {
            return ab_result;
        }

        public void setAb_result(String ab_result) {
            this.ab_result = ab_result;
        }

        public int getError() {
            return error;
        }

        public void setError(int error) {
            this.error = error;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public int getMix() {
            return mix;
        }

        public void setMix(int mix) {
            this.mix = mix;
        }

        public int getNum() {
            return num;
        }

        public void setNum(int num) {
            this.num = num;
        }

        public String getQid() {
            return qid;
        }

        public void setQid(String qid) {
            this.qid = qid;
        }

        public String getReport() {
            return report;
        }

        public void setReport(String report) {
            this.report = report;
        }

        public String getResult_type() {
            return result_type;
        }

        public void setResult_type(String result_type) {
            this.result_type = result_type;
        }
    }

    public static class ResultBean {
        /**
         * code : 0
         * msg :
         */

        private int code;
        private String msg;

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }
    }

    public static class ItemBean {
        /**
         * ar : 内地
         * class : 电影
         * da : 2018
         * dc : http://i.gtimg.cn/qqlive/img/jpgcache/files/qqvideo/hori/c/cbs8caltnjlb6t8.jpg
         * direct_report : action=101&platform=10201&qid=%2DCz01gjDSANfUmgTNaAeGOftY9qWxW4mLTBWuRlNIu48JXYev6dNqQ&srh_platform=2&ival1=1&ival2=1&ival7=11&sval1=%E4%BD%A0%E5%A5%BD&sval2=%E4%BD%A0%E5%A5%BD%2C%E4%B9%8B%E5%8D%8E&sval3=1&sval8=145_&sval4=13018129196934178682+4829128812601920753+3014707594216537690+14048088029966981454+3814984672597030378+4872936086020480038+7240110841859835766+6299957369886465477+8833120626146719341+4523549197354031742
         * ee : 0
         * et : Last Letter
         * ex : {"typeid":1}
         * id : cbs8caltnjlb6t8
         * idType : 2
         * imgtag : {"tag_1":{"id":"10012","param":"mark_vip_free","param2":"","text":"会员免费"},"tag_2":{"id":"-1","param":"","param2":"","text":""},"tag_3":{"id":"-1","param":"","param2":"","text":""},"tag_4":{"id":"40003","param":"mark_sd","param2":"","text":"蓝光"}}
         * itemType : 1
         * markLabelList :
         * pa : 周迅 杜江 秦昊
         * pd : 岩井俊二
         * ps : 1
         * report : action=101&platform=10201&qid=%2DCz01gjDSANfUmgTNaAeGOftY9qWxW4mLTBWuRlNIu48JXYev6dNqQ&srh_platform=2&ival1=1&ival2=7&ival7=11&sval1=%E4%BD%A0%E5%A5%BD&sval2=%E4%BD%A0%E5%A5%BD%2C%E4%B9%8B%E5%8D%8E&sval3=13018129196934178682&sval8=145_&sval4=13018129196934178682+4829128812601920753+3014707594216537690+14048088029966981454+3814984672597030378+4872936086020480038+7240110841859835766+6299957369886465477+8833120626146719341+4523549197354031742
         * sn : 爱情 剧情 院线
         * ss : 1
         * te : 0
         * title : <em  class="c_txt3">你好</em>,之华
         * tt : 你好，之华
         * url : https://v.qq.com/x/cover/cbs8caltnjlb6t8.html
         * word : 你好,之华
         * ec : 0
         */

        private String ar;
        @SerializedName("class")
        private String classX;
        private int da;
        private String dc;
        private String direct_report;
        private int ee;
        private String et;
        private Object ex;
        private String id;
        private int idType;
        private String imgtag;
        private int itemType;
        private Object markLabelList;
        private String pa;
        private String pd;
        private int ps;
        private String report;
        private String sn;
        private int ss;
        private int te;
        private String title;
        private String tt;
        private String url;
        private String word;
        private String ec;


        public static class MarkLabelBean {
            private String cssText;
            private String markImageUrl;
            private int position;
            private String primeText;
            private int type;
        }

        public String getAr() {
            return ar;
        }

        public void setAr(String ar) {
            this.ar = ar;
        }

        public String getClassX() {
            return classX;
        }

        public void setClassX(String classX) {
            this.classX = classX;
        }

        public int getDa() {
            return da;
        }

        public void setDa(int da) {
            this.da = da;
        }

        public String getDc() {
            return dc;
        }

        public void setDc(String dc) {
            this.dc = dc;
        }

        public String getDirect_report() {
            return direct_report;
        }

        public void setDirect_report(String direct_report) {
            this.direct_report = direct_report;
        }

        public int getEe() {
            return ee;
        }

        public void setEe(int ee) {
            this.ee = ee;
        }

        public String getEt() {
            return et;
        }

        public void setEt(String et) {
            this.et = et;
        }


        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public int getIdType() {
            return idType;
        }

        public void setIdType(int idType) {
            this.idType = idType;
        }

        public String getImgtag() {
            return imgtag;
        }

        public void setImgtag(String imgtag) {
            this.imgtag = imgtag;
        }

        public int getItemType() {
            return itemType;
        }

        public void setItemType(int itemType) {
            this.itemType = itemType;
        }


        public String getPa() {
            return pa;
        }

        public void setPa(String pa) {
            this.pa = pa;
        }

        public String getPd() {
            return pd;
        }

        public void setPd(String pd) {
            this.pd = pd;
        }

        public int getPs() {
            return ps;
        }

        public void setPs(int ps) {
            this.ps = ps;
        }

        public String getReport() {
            return report;
        }

        public void setReport(String report) {
            this.report = report;
        }

        public String getSn() {
            return sn;
        }

        public void setSn(String sn) {
            this.sn = sn;
        }

        public int getSs() {
            return ss;
        }

        public void setSs(int ss) {
            this.ss = ss;
        }

        public int getTe() {
            return te;
        }

        public void setTe(int te) {
            this.te = te;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getTt() {
            return tt;
        }

        public void setTt(String tt) {
            this.tt = tt;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getWord() {
            return word;
        }

        public void setWord(String word) {
            this.word = word;
        }

        public String getEc() {
            return ec;
        }

        public void setEc(String ec) {
            this.ec = ec;
        }

        public static class ExBean {
            /**
             * typeid : 1
             */

            private int typeid;

            public int getTypeid() {
                return typeid;
            }

            public void setTypeid(int typeid) {
                this.typeid = typeid;
            }
        }
    }
}
