config.resolve.modules.push("processedResources/js/main");

config.module.rules.push({
    test: /\.css$/,
    loader: 'style-loader!css-loader'
});