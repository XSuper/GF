/*
 * Copyright (C) 2010 The MobileSecurePay Project
 * All right reserved.
 * author: shiqun.shi@alipay.com
 * 
 *  提示：如何获取安全校验码和合作身份者id
 *  1.用您的签约支付宝账号登录支付宝网站(www.alipay.com)
 *  2.点击“商家服务”(https://b.alipay.com/order/myorder.htm)
 *  3.点击“查询合作者身份(pid)”、“查询安全校验码(key)”
 */

package com.bookingsystem.android.alipay;

//
// 请参考 Android平台安全支付服务(msp)应用开发接口(4.2 RSA算法签名)部分，并使用压缩包中的openssl RSA密钥生成工具，生成一套RSA公私钥。
// 这里签名时，只需要使用生成的RSA私钥。
// Note: 为安全起见，使用RSA私钥进行签名的操作过程，应该尽量放到商家服务器端去进行。
public final class Keys {

	//合作身份者id，以2088开头的16位纯数字
	public static final String DEFAULT_PARTNER = "2088511601113021";

	//收款支付宝账号
	public static final String DEFAULT_SELLER = "3026325242@qq.com";

	//商户私钥，自助生成
	public static final String PRIVATE = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAMGxxIMSXoeQAuyOwTaDxXMManr4xpoBMbdIEQ0cjkkyOXULJs5SyAU/VEZQ16CsLN1+WKVzFvZ+qpFy1aIFAifMPI9TRws1YgTvhs/z9a5v3wAG+zd558vuOZ0hPe2GAqgGqog58tROEwJdrHqAzunQpmmBxP7lPxZRgSgM2e5nAgMBAAECgYBR/o3gqoMHwgfq2heimy+Xn1boDa4M/ZptKv3E3iTBiZhXGsxnpRV1WzSyu4A8TQaFEPo3t7utxzsvbTxQZOxe7L9o7172+zbaNpSK5ZpnAOAZOn6tR3ufPb94IhiqqKkW9gMZA3tIT/SNOVMklzDp1aAeprG6pZwRnP/KNt1K0QJBAPo0Jsa4Yl0nl6zV6NDrYnG/JI8kuuFdcQ/lvuYYv1NCqwEgf9pxfUlwq2PopJowjx750vzAnVc07tJaJEXw36kCQQDGLnv05OWiVOWOXlIz7IORUkjwNk7FNkHxAcDRTpruxdxPHCQ8JHwlx+AzIRllttgN+lF9cBKcKRLht6eY0GePAkAFBTTd+ecnXVsCwcwJHR/9jktKf/8r9HcHTaHV9hsorYbG4AMQvAQ6jAM0Xv+mhGDqyzJ7ldXSYSKFn/9oU9v5AkAjM7SO7m2fpQbVgAmH8ZX5JS2RhYP+YcHaxl3yj8zTZs/YOX5yNGTc7SD01AdH2u+LpA//gP29QUp6T330legfAkEA6TVeRW0KxP9j2WtoCFIDYUNsKUBQ4bnLdXwo6re5ItLWGVjZ+j9u10IIh7dBrcxaqZgfEtmtGddhpIzs0DpPuA==";
	public static final String PUBLIC = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCnxj/9qwVfgoUh/y2W89L6BkRAFljhNhgPdyPuBV64bfQNN1PjbCzkIM6qRdKBoLPXmKKMiFYnkd6rAoprih3/PrQEB/VsW8OoM8fxn67UDYuyBTqA23MML9q1+ilIZwBC2AQ2UBVOrFXfFl75p6/B5KsiNG9zpgmLCUYuLkxpLQIDAQAB";

}
