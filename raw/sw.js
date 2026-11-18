var USERINFO={roles:['student'],name:'张同学',id:'194115042001',avatar:'',introduction:'智训云枢在线体验演示账号',schoolName:'',className:'软件工程1班',extraUserInfo:false,noReadMsgCount:3,setting:{},status:1};
function json(data){return new Response(JSON.stringify({code:0,data:data,msg:'ok'}),{status:200,headers:{'Content-Type':'application/json','Access-Control-Allow-Origin':'*'}});}
self.addEventListener('install',function(e){self.skipWaiting();});
self.addEventListener('activate',function(e){e.waitUntil(self.clients.claim());});
self.addEventListener('fetch',function(e){
  var u=new URL(e.request.url),p=u.pathname;
  if(p.indexOf('/apis/')<0)return;
  var s=p.split('/apis/')[1]||'';
  e.respondWith(handle(s,e.request));
});
function handle(s,req){
  if(/account\/(studentlogin|jugeLogin|login)/.test(s))return json('demo-token');
  if(s.indexOf('account/userinfo')>=0)return json(USERINFO);
  if(/account\/(logout|changePassword)/.test(s))return json('ok');
  if(/userPermission\/list/.test(s))return json([]);
  if(s.indexOf('message/countNoRead')>=0)return json(3);
  if(s.indexOf('message/get')>=0)return json([]);
  if(s.indexOf('message/read')>=0)return json('ok');
  if(req.method==='GET')return json([]);
  return json({});
}
