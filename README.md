## Lucy-XSS : XssFilter, XssPreventer  
Lucy-XSS is an open source library of two defense modules to protect Web applications from XSS attacks. It supports the white-list rule based security policy. The current default rule is Navercorp's standard. You can change the default rule if you want.

## XssFilter
- Java-based library that supports the method of setting the white-list to protect the web application.
- If you use the filter with the white-list method, it will provide tighter security measures for websites from XSS attacks than the existing filter that uses the black-list method.
- Support for both DOM and SAX.

![Lucy-XSS Filter structure.jpg](https://raw.githubusercontent.com/naver/lucy-xss-filter/master/docs/images/XssFilter_Structure.png)

## XssPreventer
- Use the apache-common-lang library to prevent XSS attack.
  (https://commons.apache.org/proper/commons-lang/javadocs/api-3.1/org/apache/commons/lang3/StringEscapeUtils.html#escapeHtml4%28java.lang.String%29)

- The difference with the XssFilter, It is a simple conversion of all strings as follows, so as not to be able to recognize the HTML tag.

```
< → &lt; 
> → &gt; 
" → &quot; 
' → &#39;
```

## Selection criteria of the XssFilter and XssPreventer
- Simple text parameter other than HTML should be filtered using the XssPreventer.
- Use Xss Filter if you need to receive HTML tags for input. (eg:  mail, visitors' book,  message board service)

## Getting started
We also offer an interactive tutorial for learning basic uses of Lucy-XSS.
See Docs for instructions on installing Luxy-xss.

## Usage examples
* XssPreventer

``` java
@Test
public void testXssPreventer() {
	String dirty = "\"><script>alert('xss');</script>";
	String clean = XssPreventer.escape(dirty);
		
	Assert.assertEquals(clean, "&quot;&gt;&lt;script&gt;alert(&#39xss&#39);&lt;/script&gt;");
	Assert.assertEquals(dirty, XssPreventer.unescape(clean));
}
```

* XssFilter : DOM

``` java
@Test
public void pairQuoteCheckOtherCase() {
	XssFilter filter = XssFilter.getInstance("lucy-xss-superset.xml");
	String dirty = "<img src=\"<img src=1\\ onerror=alert(1234)>\" onerror=\"alert('XSS')\">";
	String expected = "<img src=\"\"><!-- Not Allowed Attribute Filtered ( onerror=alert(1234)) --><img src=1\\>\" onerror=\"alert('XSS')\"&gt;";
	String clean = filter.doFilter(dirty);
	Assert.assertEquals(expected, clean);
		
	dirty = "<img src='<img src=1\\ onerror=alert(1234)>\" onerror=\"alert('XSS')\">";
	expected = "<img src=''><!-- Not Allowed Attribute Filtered ( onerror=alert(1234)) --><img src=1\\>\" onerror=\"alert('XSS')\"&gt;";
	clean = filter.doFilter(dirty);
	Assert.assertEquals(expected, clean);
}
```

* XssFilter : SAX

``` java
@Test
public void testSuperSetFix() {
	XssSaxFilter filter = XssSaxFilter.getInstance("lucy-xss-superset-sax.xml");
	String clean = "<TABLE class=\"NHN_Layout_Main\" style=\"TABLE-LAYOUT: fixed\" cellSpacing=\"0\" cellPadding=\"0\" width=\"743\">" + "</TABLE>" + "<SPAN style=\"COLOR: #66cc99\"></SPAN>";
	String filtered = filter.doFilter(clean);
	Assert.assertEquals(clean, filtered);
}
```

For more information, please see ....doc

## Contributing to Lucy
Want to hack on Lucy-XSS? Awesome! There are instructions to get you started here.
They are probably not perfect, please let us know if anything feels wrong or incomplete.
(Please wait. We are preparing for contribution guide.)

## Licensing
Lucy is licensed under the Apache License, Version 2.0. See LICENSE for full license text.

## Maintainer
[![leeplay](https://avatars1.githubusercontent.com/u/7857613?v=2&s=100)](https://github.com/leeplay)
[![Seongmin Woo](https://avatars2.githubusercontent.com/u/1201462?v=3&s=100)](https://github.com/seongminwoo)
