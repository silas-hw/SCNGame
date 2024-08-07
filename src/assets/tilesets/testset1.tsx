<?xml version="1.0" encoding="UTF-8"?>
<tileset version="1.10" tiledversion="1.10.2" name="testset1" tilewidth="8" tileheight="8" tilecount="100" columns="10">
 <image source="testset1.png" width="80" height="80"/>
 <tile id="0" probability="0.8"/>
 <tile id="1" probability="0.2"/>
 <tile id="2">
  <animation>
   <frame tileid="2" duration="200"/>
   <frame tileid="3" duration="200"/>
   <frame tileid="4" duration="200"/>
   <frame tileid="3" duration="200"/>
   <frame tileid="2" duration="200"/>
  </animation>
 </tile>
 <tile id="10" probability="0.33"/>
 <tile id="15">
  <objectgroup draworder="index" id="2">
   <object id="1" type="Wall" x="0" y="0" width="8" height="8">
    <properties>
     <property name="CollisionLayer" value="11000000"/>
     <property name="CollisionMask" value="00000000"/>
     <property name="solid" type="bool" value="true"/>
    </properties>
   </object>
  </objectgroup>
 </tile>
 <tile id="40" probability="0.333"/>
 <tile id="41" probability="0.333"/>
 <wangsets>
  <wangset name="path1" type="corner" tile="-1">
   <wangcolor name="dirt" color="#ff0000" tile="-1" probability="1"/>
   <wangcolor name="grass" color="#00ff00" tile="-1" probability="1"/>
   <wangtile tileid="0" wangid="0,2,0,2,0,2,0,2"/>
   <wangtile tileid="1" wangid="0,2,0,2,0,2,0,2"/>
   <wangtile tileid="10" wangid="0,1,0,1,0,1,0,1"/>
   <wangtile tileid="11" wangid="0,1,0,1,0,2,0,2"/>
   <wangtile tileid="12" wangid="0,2,0,2,0,1,0,1"/>
   <wangtile tileid="13" wangid="0,2,0,1,0,2,0,2"/>
   <wangtile tileid="14" wangid="0,2,0,2,0,1,0,2"/>
   <wangtile tileid="20" wangid="0,2,0,1,0,1,0,2"/>
   <wangtile tileid="21" wangid="0,1,0,1,0,1,0,2"/>
   <wangtile tileid="22" wangid="0,2,0,1,0,1,0,1"/>
   <wangtile tileid="23" wangid="0,1,0,2,0,2,0,2"/>
   <wangtile tileid="24" wangid="0,2,0,2,0,2,0,1"/>
   <wangtile tileid="30" wangid="0,1,0,2,0,2,0,1"/>
   <wangtile tileid="31" wangid="0,1,0,1,0,2,0,1"/>
   <wangtile tileid="32" wangid="0,1,0,2,0,1,0,1"/>
   <wangtile tileid="40" wangid="0,1,0,1,0,1,0,1"/>
   <wangtile tileid="41" wangid="0,1,0,1,0,1,0,1"/>
  </wangset>
 </wangsets>
</tileset>
