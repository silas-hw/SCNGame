<?xml version="1.0" encoding="UTF-8"?>
<tileset version="1.10" tiledversion="1.10.2" name="testset2" tilewidth="8" tileheight="8" spacing="2" margin="1" tilecount="4096" columns="64">
 <image source="testset2.png" width="640" height="640"/>
 <tile id="64">
  <objectgroup draworder="index" id="2">
   <object id="1" type="TerrainBox" x="0" y="0" width="8" height="8">
    <properties>
     <property name="CollisionLayer" type="class" propertytype="BitMask">
      <properties>
       <property name="0" type="bool" value="true"/>
      </properties>
     </property>
     <property name="speedCoefficient" type="float" value="0.6"/>
    </properties>
   </object>
  </objectgroup>
 </tile>
 <tile id="65">
  <objectgroup draworder="index" id="2">
   <object id="1" type="TerrainBox" x="0" y="0" width="8" height="8">
    <properties>
     <property name="CollisionLayer" type="class" propertytype="BitMask">
      <properties>
       <property name="0" type="bool" value="true"/>
      </properties>
     </property>
     <property name="speedCoefficient" type="float" value="0.6"/>
    </properties>
   </object>
  </objectgroup>
 </tile>
 <tile id="66">
  <objectgroup draworder="index" id="2">
   <object id="1" type="TerrainBox" x="0" y="0" width="8" height="8">
    <properties>
     <property name="CollisionLayer" type="class" propertytype="BitMask">
      <properties>
       <property name="0" type="bool" value="true"/>
      </properties>
     </property>
     <property name="speedCoefficient" type="float" value="0.6"/>
    </properties>
   </object>
  </objectgroup>
 </tile>
 <wangsets>
  <wangset name="stairs" type="corner" tile="-1">
   <wangcolor name="wood" color="#ff0000" tile="-1" probability="1"/>
   <wangtile tileid="0" wangid="0,0,0,1,0,0,0,0"/>
   <wangtile tileid="1" wangid="0,0,0,1,0,1,0,0"/>
   <wangtile tileid="2" wangid="0,0,0,0,0,1,0,0"/>
   <wangtile tileid="64" wangid="0,1,0,1,0,0,0,0"/>
   <wangtile tileid="65" wangid="0,1,0,1,0,1,0,1"/>
   <wangtile tileid="66" wangid="0,0,0,0,0,1,0,1"/>
   <wangtile tileid="128" wangid="0,1,0,0,0,0,0,0"/>
   <wangtile tileid="129" wangid="0,1,0,0,0,0,0,1"/>
   <wangtile tileid="130" wangid="0,0,0,0,0,0,0,1"/>
  </wangset>
 </wangsets>
</tileset>
