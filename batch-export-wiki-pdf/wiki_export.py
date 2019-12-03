# -*- coding: utf-8 -*-

import logging
import requests
import os
import time
import urlparse
import random
from pyquery import PyQuery as pq

def generateHeaders():
    headersBrower = '''
Accept:application/json, text/javascript, */*; q=0.01
Accept-Encoding:gzip, deflate, sdch
Accept-Language:en-US,en;q=0.8
Connection:keep-alive
User-Agent:Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/57.0.2987.98 Safari/537.36 OPR/44.0.2510.857
    '''
    headersBrower = '''
    Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3
Accept-Encoding: gzip, deflate
Accept-Language: zh-CN,zh;q=0.9,en;q=0.8
Connection: keep-alive
Cookie: _ga=GA1.2.185561790.1561628156; JSESSIONID=782ED6977CC49F3F65D9AA587ED30CC5; seraph.confluence=32735300%3Af45292425b28de9b59880607833b12b5cff659b0; confluence.list.pages.cookie=list-content-tree
Host: wiki.rightpaddle.cn
Referer: http://wiki.rightpaddle.cn/pages/viewpage.action?pageId=24751978
Upgrade-Insecure-Requests: 1
User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36
    '''

    headersMap = dict()
    for item in headersBrower.splitlines():
        item = str.strip(item)
        if item and ":" in item:
            (key, value) = item.split(":", 1)
            headersMap[str.strip(key)] = str.strip(value)

    return headersMap

# 如果wiki需要登录验证,先用浏览器访问wiki,登录以后,获取该用户的cookie信息. cookie信息一般包含JSESSIONID
def genereateCookies():
    cookieString = "_ga=GA1.2.185561790.1561628156; JSESSIONID=782ED6977CC49F3F65D9AA587ED30CC5; seraph.confluence=32735300%3Af45292425b28de9b59880607833b12b5cff659b0; confluence.list.pages.cookie=list-content-tree"
    cookieMap1 = {}
    for item in cookieString.split(";"):
        item = str.strip(item)
        if item and "=" in item:
            (key, value) = item.split("=", 1)
            cookieMap1[str.strip(key)] = str.strip(value)

    return cookieMap1


def save_file(url, path):
    if os.path.exists(path):
        logging.debug("exist path=" + path)
        return

    logging.debug("将 %s 保存到 %s" % (url, path))

    logging.debug("start get " + url)

    resp = requests.get(url, timeout=10, headers=generateHeaders(), cookies=genereateCookies(), stream=True)

    if resp.status_code  == 200:

        with open(path, 'wb') as f:
            for chunk in resp.iter_content(chunk_size=1024):
                if chunk:
                    f.write(chunk)
                    f.flush()
            f.close()

        logging.debug("save file " + path)

        time.sleep(random.randint(6, 15))

    else:
        print("error ", resp.status_code)

def parse_host_pageId_fromurl(url):
    r = urlparse.urlsplit(url)

    if r.port == None :
        host = r.scheme + "://" + r.netloc
    else :
        host = r.scheme + "://" + r.netloc + ":" + r.port

    params=urlparse.parse_qs(r.query,True)
    pageId = params["pageId"]

    return (host, pageId[0])


def get_sub_pages_url(parentUrl):

    url = "%s/plugins/pagetree/naturalchildren.action?decorator=none&excerpt=false&sort=position&reverse=false&disableLinks=false&expandCurrent=false&hasRoot=true&pageId=%s&treeId=0&startDepth=0" % parse_host_pageId_fromurl(parentUrl)
    resp = requests.get(url, timeout=10, headers=generateHeaders(), cookies=genereateCookies(), stream=True)

    if resp.status_code == 200:
        doc = pq(resp.text)
        links = []

        for a in doc.find("a").items():
            text = a.text().strip()
            if a.attr("href") and text:
                links.append({
                    "title" : text.encode("utf-8"),
                    "href" : urlparse.urljoin(parentUrl, a.attr("href"))
                })

        return links

    else :
        logging.error("failed get url %s status_code=%d " % (url, resp.status_code))

    return []

def export_wiki(wiki_title, wiki_page_url, dir):
    if wiki_title == u"ORI-后端中台服务".encode("utf-8"):
        return

    if not os.path.exists(dir):
        os.makedirs(dir)

    try:
        export_url = "%s/exportword?pageId=%s" % parse_host_pageId_fromurl(wiki_page_url)
        print "#####"*10 + " " + export_url + " " + "#####"*10
        save_file(export_url, dir + "/" + wiki_title + ".doc")
        subpages = get_sub_pages_url(wiki_page_url)
    except Exception as e:
        print "#####" * 10 + " " + wiki_page_url + " " + "#####" * 10
        return

    if subpages:
        parentdir = dir + "/" + wiki_title
        for subpage in subpages :
            try:
                export_wiki(subpage["title"], subpage["href"], parentdir)
            except Exception as e:
                print "error , continue"
                continue


logging.basicConfig(level=logging.DEBUG)

# 请先修改generateHeaders和genereateCookies方法的配置
wiki_page_url = "http://wiki.rightpaddle.cn/pages/viewpage.action?pageId=24751414"
wiki_title = "主端研发"
dir = "./主端研发"
export_wiki(wiki_title, wiki_page_url, dir)



