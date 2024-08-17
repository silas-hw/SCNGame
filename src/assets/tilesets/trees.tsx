<?xml version="1.0" encoding="UTF-8"?>
<tileset version="1.10" tiledversion="1.10.2" name="trees" tilewidth="16" tileheight="32" tilecount="2" columns="0">
 <grid orientation="orthogonal" width="1" height="1"/>
 <tile id="0">
  <image width="16" height="32" source="../sprites/testtree1.png"/>
  <objectgroup draworder="index" id="2">
   <object id="1" name="TreeTrunk" type="Wall" x="0" y="23" width="16" height="9">
    <properties>
     <property name="CollisionLayer" type="class" propertytype="BitMask">
      <properties>
       <property name="0" type="bool" value="true"/>
      </properties>
     </property>
    </properties>
   </object>
  </objectgroup>
 </tile>
 <tile id="1">
  <image width="16" height="16" source="../sprites/sign.png"/>
  <objectgroup draworder="index" id="2">
   <object id="1" type="Wall" x="0" y="3" width="16" height="13">
    <properties>
     <property name="CollisionLayer" type="class" propertytype="BitMask">
      <properties>
       <property name="0" type="bool" value="true"/>
      </properties>
     </property>
    </properties>
   </object>
  </objectgroup>
 </tile>
</tileset>
