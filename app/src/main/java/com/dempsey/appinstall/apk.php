<?php
/**
 * Created by PhpStorm.
 * User: zcsxj
 * Date: 2019/7/16
 * Time: 8:54
 */
$file_dir = "G:/projects/zcsxj/app/build/outputs/apk/debug/";

if (isset($_GET['n'])){
$file_name = $_GET['n'];
    if ($file_name==null|| empty($file_name)){

        $file_name = "app-debug.apk";
    }
}else{
    $file_name = "app-debug.apk";
}



if (!file_exists($file_dir . $file_name)) {

    header('HTTP/1.1 404 NOT FOUND');
} else {

    //以只读和二进制模式打开文件

    $file = fopen($file_dir . $file_name, "rb");

    //告诉浏览器这是一个文件流格式的文件

    Header("Content-type: application/octet-stream");

    //请求范围的度量单位

    Header("Accept-Ranges: bytes");

    //Content-Length是指定包含于请求或响应中数据的字节长度

    Header("Accept-Length: " . filesize($file_dir . $file_name));

    //用来告诉浏览器，文件是可以当做附件被下载，下载后的文件名称为$file_name该变量的值。

    Header("Content-Disposition: attachment; filename=" . $file_name);


    //读取文件内容并直接输出到浏览器

    echo fread($file, filesize($file_dir . $file_name));

    fclose($file);

    exit ();

}
