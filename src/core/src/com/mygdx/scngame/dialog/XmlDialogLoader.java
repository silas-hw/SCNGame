package com.mygdx.scngame.dialog;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.SynchronousAssetLoader;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.objects.EllipseMapObject;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.XmlReader;

import java.util.ArrayList;
import java.util.HashSet;

public class XmlDialogLoader extends SynchronousAssetLoader<DialogFile, XmlDialogLoader.DialogFileParameters> {

    public static final XmlReader xml = new XmlReader();
    public XmlDialogLoader(FileHandleResolver resolver) {
        super(resolver);
    }

    public XmlDialogLoader() {
        super(new InternalFileHandleResolver());
    }

    @Override
    public DialogFile load(AssetManager assetManager, String fileName, FileHandle file, DialogFileParameters parameter) {
        DialogFile dFile = new DialogFile();

        XmlReader.Element root = xml.parse(file);

        for(XmlReader.Element dialog : root.getChildrenByName("dialog")) {
            DialogNode dialogNode = new DialogNode();
            dialogNode.id = dialog.getAttribute("id");

            for(XmlReader.Element message : dialog.getChildrenByName("message")) {
                DialogMessage dialogMessage = new DialogMessage();
                dialogMessage.speaker = message.getAttribute("speaker", "");
                dialogMessage.message = message.getText();
                dialogMessage.icon = assetManager.get(message.getAttribute("icon"), Texture.class);

                dialogNode.messages.add(dialogMessage);
            }

            dFile.dialogNodes.add(dialogNode);
        }

        return dFile;
    }

    @Override
    public Array<AssetDescriptor> getDependencies(String fileName, FileHandle file, DialogFileParameters parameter) {
        XmlReader.Element root = xml.parse(file);

        HashSet<String> dependencyFiles = new HashSet<>();

        Array< XmlReader.Element> messages = root.getChildrenByNameRecursively("message");
        for(XmlReader.Element message : messages) {
            dependencyFiles.add(message.getAttribute("icon"));
        }

        Array<AssetDescriptor> dependencies = new Array<>();

        for(String dependencyFile : dependencyFiles) {
            dependencies.add(new AssetDescriptor<Texture>(dependencyFile, Texture.class));
        }

        return dependencies;
    }

    public static class DialogFileParameters extends AssetLoaderParameters<DialogFile> {

    }
}
