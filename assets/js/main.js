
document.addEventListener('DOMContentLoaded',function(){
  var menu=document.querySelector('.menu-btn');
  var sb=document.querySelector('.sidebar');
  if(menu) menu.addEventListener('click',function(){sb.classList.toggle('open')});
  document.querySelectorAll('.sidebar a').forEach(function(a){a.addEventListener('click',function(){sb.classList.remove('open')})});
  // 目录高亮
  var links=[].slice.call(document.querySelectorAll('.toc a'));
  if(links.length && window.IntersectionObserver){
    var obs=new IntersectionObserver(function(es){
      es.forEach(function(e){if(e.isIntersecting){
        links.forEach(function(l){l.classList.toggle('active',l.getAttribute('href')==='#'+e.target.id)});
      }})
    },{rootMargin:'-80px 0px -70% 0px'});
    document.querySelectorAll('h2[id],h3[id]').forEach(function(h){obs.observe(h)});
  }
});
